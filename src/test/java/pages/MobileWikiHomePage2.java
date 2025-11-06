package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MobileWikiHomePage2 {
    private final WebDriver driver;

    // TODO: swap to real ANDROID locators (id/xpath/accessibility) after inspection
    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By signInButton  = By.id("signIn");

    public MobileWikiHomePage2(WebDriver driver) {
        this.driver = driver;
    }

    public void enterUsername(String username) {
        driver.findElement(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickSignIn() {
        driver.findElement(signInButton).click();
    }
}
