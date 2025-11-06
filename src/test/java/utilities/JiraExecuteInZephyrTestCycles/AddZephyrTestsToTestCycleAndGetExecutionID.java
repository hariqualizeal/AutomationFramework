package utilities.JiraExecuteInZephyrTestCycles;

import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import com.thed.zephyr.cloud.rest.client.JwtGenerator;
import io.restassured.response.Response;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class AddZephyrTestsToTestCycleAndGetExecutionID {
  public static void main(String[] args) throws URISyntaxException, IllegalStateException {
    // Generating jwt token first
    // only base url should be used to generate the jwt (not the complete url)
    String baseURLZephyr = "https://prod-api.zephyr4jiracloud.com/connect";
    String accessKeyZephyr = "zephyr access key";
    String secretKeyZephyr = "zephyr secret key";
    String accIDJira = "account id from jira";

    ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(baseURLZephyr, accessKeyZephyr, secretKeyZephyr, accIDJira).build();
    JwtGenerator jwtGenerator = client.getJwtGenerator();
    String uriAddTests = baseURLZephyr + "/public/rest/api/1.0/execution";
    URI uri = new URI(uriAddTests);
    int expirationInSec = 600;
    String jwt = jwtGenerator.generateJWT("POST", uri, expirationInSec);

    Map<String, Object> bodyData = new HashMap<>();
    bodyData.put("projectID", 12725);
    bodyData.put("issueID", 6546);
    bodyData.put("cycleID", "cycle id fetched using CreateTestCycleInZephyrSquadCloudAndGetCycleID.java class");
    bodyData.put("versionID", "-1"); //version id is -1 for adhoc cycle

    Response responseAddedTestCycle =
            given()
                    .header("zapiAccessKey", accessKeyZephyr)
                    .header("Authorization", jwt)
                    .header("Content-Type", "application/json")
                    .body(bodyData)
                    .when()
                    .post(uri)
                    .then()
                    .extract()
                    .response();

    System.out.println("created test cycle in zephyr squad cloud");
    String executionID = responseAddedTestCycle.jsonPath().getString("execution.id");
    System.out.println("execution id" + executionID);
  }
}
