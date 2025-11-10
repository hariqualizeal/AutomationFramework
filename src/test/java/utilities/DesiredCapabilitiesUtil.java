package utilities;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Reporter;

public class DesiredCapabilitiesUtil {

  public DesiredCapabilities getDesiredCapabilitiesMobile(String deviceName, String platformVersion) {
    boolean isCloud = Reporter.getCurrentTestResult()
            .getTestContext().getCurrentXmlTest()
            .getParameter("Cloud").equalsIgnoreCase("true");

    String browserStackAppURLAndroid = ConfigReader.get("BrowserStackAppURLAndroid");
    String browserStackAppURLIos = ConfigReader.get("BrowserStackAppURLIos");
    DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

    if (isCloud) {
      desiredCapabilities.setCapability("deviceName", deviceName);
      desiredCapabilities.setCapability("appPackage", "org.wikipedia.alpha");
      desiredCapabilities.setCapability("appActivity", "org.wikipedia.main.MainActivity");
      if (Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
              .getParameter("platform").equalsIgnoreCase("android")) {
        desiredCapabilities.setCapability("app", browserStackAppURLAndroid);
      } else {
        desiredCapabilities.setCapability("app", browserStackAppURLIos);
      }
      desiredCapabilities.setCapability("browserstack.video", "true");
      desiredCapabilities.setCapability("project", "Mobile Automation Project");
      desiredCapabilities.setCapability("build", "Mobile Automation Build");
      desiredCapabilities.setCapability("name", "Mobile Automation Name");
      desiredCapabilities.setCapability("platformVersion", platformVersion);
      desiredCapabilities.setCapability("skipUnlock", "true");
      desiredCapabilities.setCapability("noReset", "false");
    } else {
      desiredCapabilities.setCapability("platformName", "Android");
      desiredCapabilities.setCapability("appium:platformVersion", platformVersion);
      desiredCapabilities.setCapability("appium:deviceName", deviceName);
      desiredCapabilities.setCapability("appium:appPackage", "org.wikipedia.alpha");
      desiredCapabilities.setCapability("appium:appActivity", "org.wikipedia.main.MainActivity");
      desiredCapabilities.setCapability("appium:noReset", false);
      desiredCapabilities.setCapability("appium:skipUnlock", true);
      desiredCapabilities.setCapability("appium:automationName", "UiAutomator2");
    }
    return desiredCapabilities;
  }

  public DesiredCapabilities getDesiredCapabilitiesWebCloud(String platform, String platformVersion, String browser) {
    boolean isCloud = Reporter.getCurrentTestResult()
            .getTestContext().getCurrentXmlTest()
            .getParameter("Cloud").equalsIgnoreCase("true");

    DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

    if (isCloud) {
      desiredCapabilities.setCapability("os", platform);
      desiredCapabilities.setCapability("os_version", platformVersion);
      desiredCapabilities.setCapability("browser", browser);
      if (platform.toLowerCase().contains("windows")) {
        desiredCapabilities.setCapability("resolution", "1366x768");
      } else if (platform.toLowerCase().contains("os x")) {
        desiredCapabilities.setCapability("resolution", "1280x960");
      }
      if (browser.equalsIgnoreCase("safari")) {
        desiredCapabilities.setCapability("browser_version", "15.1");
      } else {
        desiredCapabilities.setCapability("browser_version", "latest");
      }
      desiredCapabilities.setCapability("browserstack.video", "true");
      desiredCapabilities.setCapability("project", "ProjectName");
      desiredCapabilities.setCapability("build", "BuildName");
      desiredCapabilities.setCapability("name", "TestName");
    }

    return desiredCapabilities;
  }
}