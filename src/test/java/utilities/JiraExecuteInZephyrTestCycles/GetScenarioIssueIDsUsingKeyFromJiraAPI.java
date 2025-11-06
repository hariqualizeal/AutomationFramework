package utilities.JiraExecuteInZephyrTestCycles;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetScenarioIssueIDsUsingKeyFromJiraAPI {
  public static void main(String[] args) {
    String issueKey = "QA-3";
    String gerURIForScenarioIssueIDsBasedOnKeyFromJira =
            "http://eastern.atlassian.net/rest/api/2/issue" + issueKey;

    Response responseScenarioIssueIDsBasedOnKeyFromJira =
            given()
                    .header("Authorization", "authorization token from jira")
                    .when()
                    .get(gerURIForScenarioIssueIDsBasedOnKeyFromJira);

    String scenarioIssueIDsUsingKeyFromJiraAPI =
            responseScenarioIssueIDsBasedOnKeyFromJira.jsonPath().getString("id");
    System.out.println(scenarioIssueIDsUsingKeyFromJiraAPI);
  }
}
