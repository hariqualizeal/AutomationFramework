package cucumber.stepdefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.testng.Reporter;
import utilities.ThreadLocalDriver;

/**
 * this class contains java implementation for steps in feature files
 */
public class MyntraHomePageStepDefinitions extends BaseSteps {

    boolean mobile = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("WebOrMobile").equalsIgnoreCase("Mobile");
    boolean webCloud = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("WebOrMobile").equalsIgnoreCase("WebCloud");

    @Before
    public void setupLoginSteps() {
        if (mobile) {
            //mobile code - for both cloud/local
            setupScreensMobile(ThreadLocalDriver.getAppiumDriverThreadLocal());
        } else if (webCloud) {
            //web code - for cloud
            setupScreensWebCloud(ThreadLocalDriver.getRemoteWebDriverThreadLocal());
        } else {
            //web code - for local
            setupScreensWebLocal(ThreadLocalDriver.getWebDriverThreadLocal());
        }
    }

    @Given("User is on home page")
    public void userisonhomepageandtapsCategories() {
        if (mobile) {
            //mobile code - for both cloud/local
        } else if (webCloud) {
            //web code - for cloud
            webCloudMyntraHomePage.userOnHomePage();
        } else {
            //web code - for local
            webLocalMyntraHomePage.userOnHomePage();
        }
    }

    @When("User clicks Studio {int} Two")
    public void userclicksStudio(int index) {
        if (mobile) {
            //mobile code - for both cloud/local
        } else if (webCloud) {
            webCloudMyntraHomePage.clickStudio();
        } else {
            webLocalMyntraHomePage.clickStudio();
        }
    }

    @When("User clicks on Categories One")
    public void userClicksOnCategories() {
        if (mobile) {
            //mobile code - for both cloud/local
        } else if (webCloud) {
            webCloudMyntraHomePage.clickCategories();
        } else {
            webLocalMyntraHomePage.clickCategories();
        }
    }
}
