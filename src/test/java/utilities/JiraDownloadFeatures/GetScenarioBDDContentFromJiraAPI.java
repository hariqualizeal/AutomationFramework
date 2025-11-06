package utilities.JiraDownloadFeatures;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetScenarioBDDContentFromJiraAPI {
  public static void main(String[] args) {

    //get this issue id using GetScenarioIssueIDsFromJiraAPI.java class
    String scenarioIssueIDFsromJira = "5234";

    String getURIForScenarioJiraDataFromJira =
            "http://eastern.atlassian.net/rest/api/2/issue/" + scenarioIssueIDFsromJira;
    Response responseScenarioJiraDataFromJira =
            given()
                    .header("Authorization", "authorization token from jira")
                    .when()
                    .get(getURIForScenarioJiraDataFromJira);

    String scenarioFeatureIDFromJira =
            responseScenarioJiraDataFromJira
                    .jsonPath()
                    .getString("fields.issueLinks.inwardIssue.id")
                    .replace("[", "")
                    .replace("]", "");
    String scenarioFeatureKeyFromJira =
            responseScenarioJiraDataFromJira
                    .jsonPath()
                    .getString("fields.issueLinks.inwardIssue.key")
                    .replace("[", "")
                    .replace("]", "");
    String scenarioKeyFromJira =
            responseScenarioJiraDataFromJira
                    .jsonPath()
                    .getString("key")
                    .replace("[", "")
                    .replace("]", "");
    System.out.println(scenarioKeyFromJira); //e.g. QA5
    System.out.println(scenarioIssueIDFsromJira); //e.g. 5643
    System.out.println(scenarioFeatureKeyFromJira); //e.g. QA6
    System.out.println(scenarioFeatureIDFromJira); //e.g. 5643

    String scenarioLinkedFeatureSummaryFromJira =
            responseScenarioJiraDataFromJira
                    .jsonPath()
                    .getString("fields.issuelinks.inwardIssue.fields.summary")
                    .replace("[", "")
                    .replace("]", "");
    System.out.println(scenarioLinkedFeatureSummaryFromJira);

    String scenarioTagsFromJira = "@" +
            responseScenarioJiraDataFromJira
                    .jsonPath()
                    .getString("fields.labels")
                    .replace("[", "")
                    .replace("]", "")
                    .replace(", ", "\n@");
    System.out.println(scenarioTagsFromJira);

    String scenarioSummaryFromJira =
            responseScenarioJiraDataFromJira
                    .jsonPath()
                    .getString("fields.summary");
    System.out.println(scenarioSummaryFromJira);
  }
}
