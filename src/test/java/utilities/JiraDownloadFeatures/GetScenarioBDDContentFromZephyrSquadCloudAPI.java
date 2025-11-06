package utilities.JiraDownloadFeatures;

import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import com.thed.zephyr.cloud.rest.client.JwtGenerator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.net.URI;
import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;

/**
 * this class used to fetch all the scenario bdd steps including examples
 */
public class GetScenarioBDDContentFromZephyrSquadCloudAPI {
  public static void main(String[] args) throws URISyntaxException {
    // only base url should be used to generate the jwt (not the complete url)
    String baseURLZephyr = "https://prod-api.zephyr4jiracloud.com/connect";
    String accessKeyZephyr = "zephyr access key";
    String secretKeyZephyr = "zephyr secret key";
    String accIDJira = "account id from jira";
    String scenarioIssueIdFromJira = "5566";

    ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(baseURLZephyr, accessKeyZephyr, secretKeyZephyr, accIDJira).build();
    JwtGenerator jwtGenerator = client.getJwtGenerator();
    String getURIScenarioBDDContentFromZephyr = baseURLZephyr + "/public/rest/api/1.0/integration/bddcontent" + scenarioIssueIdFromJira;
    URI uriScenarioZephyr = new URI(getURIScenarioBDDContentFromZephyr);
    int expirationInSec = 600;
    String jwt = jwtGenerator.generateJWT("GET", uriScenarioZephyr, expirationInSec);

    Response responseScenarioBDDContentFromZephyr =
            given()
                    .header("zapiAccessKey", accessKeyZephyr)
                    .header("Authorization", jwt)
                    .when()
                    .get(getURIScenarioBDDContentFromZephyr);

    String responsePrettyStringScenarioBDDContentFromZephyr =
            responseScenarioBDDContentFromZephyr.getBody().asPrettyString();
    JsonPath jsonPath = new JsonPath(responsePrettyStringScenarioBDDContentFromZephyr);
    String bddContent = jsonPath.getString("data.attributes.content");
    System.out.println(bddContent);
  }
}
