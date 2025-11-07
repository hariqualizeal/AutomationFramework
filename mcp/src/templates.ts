export type Platforms = "android" | "ios";

export function pageTemplate(pkg: string, className: string, platform: Platforms) {
  return `package ${pkg};

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ${platform === "android" ? "io.appium.java_client.android.AndroidDriver" : "io.appium.java_client.ios.IOSDriver"};
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import cucumber.ThreadLocalDriver; // your existing TL driver wrapper

public class ${className} {

    private final org.openqa.selenium.WebDriver driver;

    public ${className}() {
        this.driver = ThreadLocalDriver.getDriver();
    }

    // Locators (By only)
    private By username = By.id("username_input");
    private By password = By.id("password_input");
    private By signIn  = By.id("signin_button");
    private By successBanner = By.xpath("//*[@text='Login successful' or @name='Login successful']");

    private WebElement waitFor(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void launchApp() {
        // no-op here; app launch is handled by your TestNG runner and DesiredCapabilities
    }

    public void enterUsername(String value) {
        waitFor(username).sendKeys(value);
    }

    public void enterPassword(String value) {
        waitFor(password).sendKeys(value);
    }

    public void tapSignIn() {
        waitFor(signIn).click();
    }

    public boolean isLoginSuccessful() {
        try {
            return waitFor(successBanner).isDisplayed();
        } catch (Exception ex) {
            return false;
        }
    }
}
`;
}

export function stepDefTemplate(pkg: string, pageFqn: string) {
  return `package ${pkg};

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.testng.Assert;

public class WikiHomePageStepDefinitions {

    private final ${pageFqn} page = new ${pageFqn}();

    @Given("User launches mobile app")
    public void userLaunchesMobileApp() {
        page.launchApp();
    }

    @When("User enters username {string} and password {string}")
    public void userEntersUsernameAndPassword(String u, String p) {
        page.enterUsername(u);
        page.enterPassword(p);
    }

    @When("User clicks on signin")
    public void userClicksOnSignin() {
        page.tapSignIn();
    }

    @Then("User verifies whether login is successful")
    public void userVerifiesLoginSuccess() {
        Assert.assertTrue(page.isLoginSuccessful(), "Login was not successful");
    }
}
`;
}

export function featureTemplate() {
  return `Feature: Verify user login

  Scenario: Successful login
    Given User launches mobile app
    When User enters username "abc@gmail.com" and password "password@123"
    And User clicks on signin
    Then User verifies whether login is successful
`;
}
