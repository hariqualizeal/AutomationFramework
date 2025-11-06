package pages;

import io.appium.java_client.AppiumDriver;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MobileWikiHomePage extends BaseScreenMobile {
    public MobileWikiHomePage(AppiumDriver driver) {
        super(driver);
    }

    /**
     * Mobile Elements
     */
    private final By OKAndroid = By.xpath("//*[@name='OK' or @text='OK']");
    private final By OKIOS = By.xpath("//*[@name='username' or @label='username']");

    private By Ok() {
      return isAndroid() ? OKAndroid : OKIOS;
    }

    private final By allowAndroid = By.xpath("//*[@name='Allow' or @text='Allow']");
    private final By allowIOS = By.xpath("//*[@name='username' or @label='username']");

    private By allow() {
        return isAndroid() ? allowAndroid : allowIOS;
    }

    private final By ListByAndroid = By.xpath("(//*[@resource-id='org.wikipedia.alpha:id/icon'])[2]");
    private final By ListByIOS = By.xpath("//*[@name='username' or @label='username']");

    private By ListBy() {
        return isAndroid() ? ListByAndroid : ListByIOS;
    }

    private final By HistoryOrOkAndroid = By.xpath("//*[@resource-id='com.example:id/password' or @content-desc='password']");
    private final By HistoryOrOkIOS = By.xpath("//*[@name='password' or @label='password']");

    private By HistoryOrOk() {
        return isAndroid() ? HistoryOrOkAndroid : HistoryOrOkIOS;
    }


    /**
     * Actions
     */
    @SneakyThrows
    public void userOnWikiHomePage() {
        if(elementDisplayed(Ok())) {
            waitAndClick(Ok());
        }
        if(elementDisplayed(allow())) {
            waitAndClick(allow());
        }
    }

    @SneakyThrows
    public void clickListElement() {
        waitAndClick(ListBy());
//    test.get().log(Status.INFO, "Clicked List/Alert", MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshotAsBase64()).build());
    }

    @SneakyThrows
    public void clickHistoryElement() {
        waitAndClick(HistoryOrOk());
//    test.get().log(Status.INFO, "Clicked History/OK", MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshotAsBase64()).build());
    }
}