package cucumber.stepdefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.testng.Reporter;
import utilities.ThreadLocalDriver;

public class WikiHomePageStepDefinitions extends BaseSteps {

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

  @Given("User is on Wiki home page")
  public void userIsOnBSHomePage() {
    if (mobile) {
      //mobile code - for both cloud/local
      mobileWikiHomePage.userOnWikiHomePage();
    } else if (webCloud) {
      //web code - for cloud
    } else {
      //web code - for local
    }
  }

  @When("User clicks List One")
  public void userClicksList() {
    if (mobile) {
      //mobile code - for both cloud/local
      mobileWikiHomePage.clickListElement();
    } else if (webCloud) {
      //web code - for cloud
    } else {
      //web code - for local
    }
  }

  @When("User clicks History Two")
  public void userClicksButton() {
    if (mobile) {
      //mobile code - for both cloud/local
      mobileWikiHomePage.clickHistoryElement();
    } else if (webCloud) {
      //web code - for cloud
    } else {
      //web code - for local
    }
  }
}
