package utilities.JiraDownloadFeatures;

import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class GetScenarioIssueIDsUsingTagFromJiraAPI {
  public static void main(String[] args){
    String gerURIForScenarioIssueIDsBasedOnTagFromJira =
            "http://eastern.atlassian.net/rest/api/2/search";
    String tag = "login";
    String jqlFilter = "labels="+ tag +"&project=QA";
    Response responseScenarioIssueIDsBasedOnTagsFromJira =
            given()
                    .queryParam("jql", jqlFilter)
                    .queryParam("fields", "id")
                    .header("Authorization","authorization token from jira")
                    .when()
                    .get(gerURIForScenarioIssueIDsBasedOnTagFromJira);

    List<String> scenarioIssueIDsFromJiraAPI =
            responseScenarioIssueIDsBasedOnTagsFromJira.jsonPath().getList("issues.id");
    System.out.println(scenarioIssueIDsFromJiraAPI);
  }
}
