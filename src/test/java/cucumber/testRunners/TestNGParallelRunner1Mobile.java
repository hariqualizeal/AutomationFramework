package cucumber.testRunners;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Reporter;
import org.testng.annotations.*;
import utilities.ConfigReader;
import utilities.DesiredCapabilitiesUtil;
import utilities.ThreadLocalDriver;

import java.io.IOException;
import java.net.URL;

/**
 * This class uses multithreading to run testRunners parallel
 */
@CucumberOptions(
        monochrome = true,
        tags = "@WikiScenario",
        features = "src/test/resources/features",
        glue = "cucumber.stepdefinitions",
        publish = false,
        plugin = {"listener.CucumberListener", "pretty",
                "html:target/cucumber-reports/CucumberReport2.html",
                "json:target/cucumber-reports/cucumber-report2.json"}
)
public class TestNGParallelRunner1Mobile {

  private TestNGCucumberRunner testNGCucumberRunner;
  private final DesiredCapabilitiesUtil desiredCapabilitiesUtil = new DesiredCapabilitiesUtil();

  @BeforeClass(alwaysRun = true)
  public void setUpClass() {
    testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
  }

  @BeforeMethod
  @Parameters({"deviceName", "platformVersion"})
  public void setup(String deviceName, String platformVersion) throws IOException {
    String browserStackUsername = ConfigReader.get("BrowserStackUsername");
    String browserStackAccessKey = ConfigReader.get("BrowserStackAccessKey");
    String browserStackServer = ConfigReader.get("BrowserStackServer");
    DesiredCapabilities caps = desiredCapabilitiesUtil.getDesiredCapabilitiesMobile(deviceName, platformVersion);
    if (Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("Cloud").equalsIgnoreCase("true")) {
      if (Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("platform").equalsIgnoreCase("android"))
        ThreadLocalDriver.setAppiumDriverThreadLocal(new AndroidDriver(new URL("http://" + browserStackUsername + ":" + browserStackAccessKey + "@" + browserStackServer + "/wd/hub"), caps));
      else
        ThreadLocalDriver.setAppiumDriverThreadLocal(new IOSDriver(new URL("http://" + browserStackUsername + ":" + browserStackAccessKey + "@" + browserStackServer + "/wd/hub"), caps));
    } else {
      ThreadLocalDriver.setAppiumDriverThreadLocal(new AndroidDriver(new URL("http://127.0.0.1:4723"), caps));
    }
  }

  @Test(groups = "cucumber", description = "Run Cucumber Features.", dataProvider = "scenarios")
  public void scenario(PickleWrapper pickleWrapper, FeatureWrapper featureWrapper) {
    testNGCucumberRunner.runScenario(pickleWrapper.getPickle());
  }

  /**
   * Returns two dimensional array of PickleEventWrapper scenarios
   * with their associated CucumberFeatureWrapper feature.
   *
   * @return a two dimensional array of scenarios features.
   */
  @DataProvider
  public Object[][] scenarios() {
    return testNGCucumberRunner.provideScenarios();
  }

  @AfterMethod
  public synchronized void teardown() {
    ThreadLocalDriver.getAppiumDriverThreadLocal().quit();
  }

  @AfterClass(alwaysRun = true)
  public void tearDownClass() {
    testNGCucumberRunner.finish();
  }
}