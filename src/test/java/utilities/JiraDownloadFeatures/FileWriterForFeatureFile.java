package utilities.JiraDownloadFeatures;

import java.io.FileWriter;
import java.io.IOException;

public class FileWriterForFeatureFile {
  public static void main(String[] args) throws IOException {
    String directory = "directory path";
    String scenarioKeyFromJira = "scenario key fetched using GetScenarioBDDContentFromJiraAPI.java";
    String scenarioLinkedFeatureSummary = "fetched using GetScenarioBDDContentFromJiraAPI.java";
    String scenarioTagsFromJira = "fetched using GetScenarioBDDContentFromJiraAPI.java";
    String scenarioSummaryFromJira = "fetched using GetScenarioBDDContentFromJiraAPI.java";
    String scenarioBDDContentFromZephyr = "fetched using GetScenarioBDDContentFromZephyrSquadCloudAPI.java";

    FileWriter fileWriter = new FileWriter(directory+scenarioKeyFromJira+".feature");
    fileWriter.write(
            "@BDD_Feature"+"\nFeature: "+scenarioLinkedFeatureSummary+"\n"+
                    "@BDDTEST-"+scenarioKeyFromJira+"\n"+scenarioTagsFromJira+"\n"+
                    "Scenario Outline: "+ scenarioSummaryFromJira+"\n"+
                    scenarioBDDContentFromZephyr);
    fileWriter.close();
  }
}
