package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utilities.ConfigReader;
import utilities.ThreadLocalDriver;

public class WebLocalMyntraHomePage extends BaseScreenWeb {
  public WebLocalMyntraHomePage(WebDriver driver) {super(driver);}

  /**
   * Web Elements
   */
  By womenCategoryButton = By.xpath("//*[text()='Women']");
  By kidsCategoryButton = By.xpath("//*[text()='Kids']");

  /**
   * Actions
   */
  public void userOnHomePage() {
    String onlineUrl = ConfigReader.get("OnlineUrl");
    ThreadLocalDriver.getWebDriverThreadLocal().get(onlineUrl);
//      test.get().log(Status.INFO, "Home Page", MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshotAsBase64WebLocal()).build());
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