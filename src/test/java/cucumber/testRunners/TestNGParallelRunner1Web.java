package cucumber.testRunners;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
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
        tags = "@MyntraScenario",
        features = "src/test/resources/features",
        glue = "cucumber.stepdefinitions",
        publish = false,
        plugin = {"listener.CucumberListener", "pretty",
                "html:target/cucumber-reports/CucumberReport2.html",
                "json:target/cucumber-reports/cucumber-report2.json"}
)
public class TestNGParallelRunner1Web {

  private TestNGCucumberRunner testNGCucumberRunner;
  private final DesiredCapabilitiesUtil desiredCapabilitiesUtil = new DesiredCapabilitiesUtil();

  @BeforeClass(alwaysRun = true)
  public void setUpClass() {
    testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
  }

  @BeforeMethod
  @Parameters({"platform", "platformVersion", "browser"})
  public void setup(String platform, String platformVersion, String browser) throws IOException {
    String browserStackUsername = ConfigReader.get("BrowserStackUsername");
    String browserStackAccessKey = ConfigReader.get("BrowserStackAccessKey");
    String browserStackServer = ConfigReader.get("BrowserStackServer");
    DesiredCapabilities caps = desiredCapabilitiesUtil.getDesiredCapabilitiesWebCloud(platform, platformVersion, browser);
    if (Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("Cloud").equalsIgnoreCase("true")) {
      ThreadLocalDriver.setRemoteWebDriverThreadLocal(new RemoteWebDriver(new URL("http://" + browserStackUsername + ":" + browserStackAccessKey + "@" + browserStackServer + "/wd/hub"), caps));
    } else {
      ThreadLocalDriver.setWebDriverThreadLocal(new ChromeDriver());
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
    if (Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("WebOrMobile").equalsIgnoreCase("WebCloud"))
      ThreadLocalDriver.getRemoteWebDriverThreadLocal().quit();
    else
      ThreadLocalDriver.getWebDriverThreadLocal().quit();
  }

  @AfterClass(alwaysRun = true)
  public void tearDownClass() {
    testNGCucumberRunner.finish();
  }
}