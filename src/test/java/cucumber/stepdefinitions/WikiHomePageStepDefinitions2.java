package cucumber.stepdefinitions;

import io.cucumber.java.en.*;
import pages.MobileWikiHomePage2;
import org.openqa.selenium.WebDriver;

public class WikiHomePageStepDefinitions2 {

    private WebDriver driver; // TODO: wire your ThreadLocal/Appium driver
    private MobileWikiHomePage2 homePage;

    @Given("User launches mobile app")
    public void user_launches_mobile_app() {
        // driver = ThreadLocalDriver.getDriver(); // e.g., your existing utility
        homePage = new MobileWikiHomePage2(driver);
    }

    @When("User enters username and password")
    public void user_enters_username_and_password() {
        homePage.enterUsername("abc@gmail.com");
        homePage.enterPassword("password@123");
    }

    @And("User clicks on signin")
    public void user_clicks_on_signin() {
        homePage.clickSignIn();
    }

    @Then("User verifies whether login is successful")
    public void user_verifies_login_success() {
        // TODO: Assert on a post-login element
    }
}
