package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Reporter;
import utilities.ConfigReader;
import utilities.ThreadLocalDriver;

public class WebCloudMyntraHomePage extends BaseScreenWeb {
    public WebCloudMyntraHomePage(RemoteWebDriver driver) {
        super(driver);
    }

  /**
   * Web Elements
   */
    By womenCategoryButton = By.xpath("//*[text()='Women']");
    By kidsCategoryButton = By.xpath("//*[text()='Kids']");

    /**
     * Actions
     */
    public void userOnHomePage() {
        ConfigReader configReader = new ConfigReader();
        String onlineUrl = configReader.config().getProperty("OnlineUrl");
        ThreadLocalDriver.getRemoteWebDriverThreadLocal().get(onlineUrl);
//      test.get().log(Status.INFO, "Home Page", MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshotAsBase64WebCloud()).build());
    }

    public void clickStudio() {
        waitAndClick(kidsCategoryButton);
//    test.get().log(Status.INFO, "Clicked Studio", MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshotAsBase64WebCloud()).build());
    }

    public void clickCategories() {
        waitAndClick(womenCategoryButton);
//    test.get().log(Status.INFO, "Clicked Home", MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshotAsBase64WebLocal()).build());
    }
}
