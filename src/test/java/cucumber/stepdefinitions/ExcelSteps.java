package cucumber.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utilities.ExcelUtil;
import utilities.ThreadLocalDriver;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ExcelSteps {

    private List<String> states;

    @Given("I read the {string} sheet column {string} from {string}")
    public void i_read_the_sheet_column_from(String sheetName, String columnHeader, String fileName) throws Exception {
        Path excelPath = Paths.get(System.getProperty("user.dir"),"\\src\\test\\resources\\excelfiles\\"+ fileName);
        ExcelUtil excel = new ExcelUtil(excelPath);
        states = excel.getColumn(sheetName, columnHeader);
        ThreadLocalDriver.getWebDriverThreadLocal().get("https://rimcentral5.rimsys.io/");
    }

    @Then("I should have {int} products")
    public void i_should_have_count_states(Integer expected) {
        int count=0;
        for (String s : states){
            System.out.println(s);
            //verify products code
            count++;
        }
        System.out.println("count "+count);
    }

    @Then("the list should include {string} and {string}")
    public void the_list_should_include_and(String s1, String s2) {
/*
        Assert.assertTrue("Missing " + s1, states.contains(s1));
        Assert.assertTrue("Missing " + s2, states.contains(s2));
*/
    }
}