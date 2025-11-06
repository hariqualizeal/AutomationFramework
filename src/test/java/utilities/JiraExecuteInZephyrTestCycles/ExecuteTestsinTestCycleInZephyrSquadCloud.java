package utilities.JiraExecuteInZephyrTestCycles;

import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import com.thed.zephyr.cloud.rest.client.JwtGenerator;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ExecuteTestsinTestCycleInZephyrSquadCloud {
  public static void main(String[] args) throws URISyntaxException, IllegalStateException {
    // Generating jwt token first
    // only base url should be used to generate the jwt (not the complete url)
    String baseURLZephyr = "https://prod-api.zephyr4jiracloud.com/connect";
    String accessKeyZephyr = "zephyr access key";
    String secretKeyZephyr = "zephyr secret key";
    String accIDJira = "account id from jira";

    ZFJCloudRestClient client =
            ZFJCloudRestClient.restBuilder(baseURLZephyr, accessKeyZephyr, secretKeyZephyr, accIDJira)
                    .build();
    JwtGenerator jwtGenerator = client.getJwtGenerator();
    String uriExecuteTestCase = baseURLZephyr + "/public/rest/api/1.0/executions";
    URI uri = new URI(uriExecuteTestCase);
    int expirationInSec = 600;
    String jwt = jwtGenerator.generateJWT("POST", uri, expirationInSec);

    String executionID = "get it from AddZephyrTestsToTestCycleAndGetExecutionID.java class";
    String status = "1"; //1 for passed, 2 for failed

    List<String> executionIDs = new ArrayList<>();
    executionIDs.add(executionID);
    Map<String, Object> bodyData = new HashMap<>();
    bodyData.put("executions", executionIDs);
    bodyData.put("status", status);
    bodyData.put("clearDefectMappingFlag", false);
    bodyData.put("testStepStatusChangeFlag", true);
    bodyData.put("stepStatus", -1);

    given()
            .header("zapiAccessKey", accessKeyZephyr)
            .header("Authorization", jwt)
            .header("Content-Type", "application/json")
            .body(bodyData)
            .when()
            .post(uri);

    System.out.println("Updated scenario execution status in Jira Zephyr Test Cycle");
  }
}
