package listener;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Reporter;
import utilities.ThreadLocalExtent;

import static utilities.ThreadLocalDriver.*;

public class CucumberListener implements ConcurrentEventListener {
    public static ExtentReports extentReports = ThreadLocalExtent.createInstance();

    public static final ThreadLocal<ExtentTest> extentTestThreadLocalTestCase = new ThreadLocal<>();
    public static final ThreadLocal<ExtentTest> extentTestThreadLocalTestStep = new ThreadLocal<>();

    boolean mobile = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("WebOrMobile").equalsIgnoreCase("Mobile");
    boolean webCloud = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("WebOrMobile").equalsIgnoreCase("WebCloud");
    boolean webLocal = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("WebOrMobile").equalsIgnoreCase("WebLocal");

    public static String takeScreenshotAsBase64Mobile() {
        return ((TakesScreenshot) getAppiumDriverThreadLocal()).getScreenshotAs(OutputType.BASE64);
    }

    public static String takeScreenshotAsBase64WebCloud() {
        return ((TakesScreenshot) getRemoteWebDriverThreadLocal()).getScreenshotAs(OutputType.BASE64);
    }

    public static String takeScreenshotAsBase64WebLocal() {
        return ((TakesScreenshot) getWebDriverThreadLocal()).getScreenshotAs(OutputType.BASE64);
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, eventHandlerTestRunStarted);
        publisher.registerHandlerFor(TestCaseStarted.class, eventHandlerTestCaseStarted);
        publisher.registerHandlerFor(TestStepStarted.class, eventHandlerTestStepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, eventHandlerTestStepFinished);
        publisher.registerHandlerFor(TestCaseFinished.class, eventHandlerTestCaseFinished);
        publisher.registerHandlerFor(TestRunFinished.class, eventHandlerTestRunFinished);
    }

    public EventHandler<TestRunStarted> eventHandlerTestRunStarted = new EventHandler<TestRunStarted>() {
        public void receive(TestRunStarted event) {
            System.out.println("Cucumber Event TestRunStarted");
        }
    };
    public EventHandler<TestCaseStarted> eventHandlerTestCaseStarted = new EventHandler<TestCaseStarted>() {
        public void receive(TestCaseStarted event) {
            String testScenarioName = event.getTestCase().getName();
            if (mobile) {
                if (String.valueOf(getAppiumDriverThreadLocal().getCapabilities().getCapability("deviceName")).toLowerCase().contains("iphone")) {
                    // ios
                    String deviceName = String.valueOf(getAppiumDriverThreadLocal().getCapabilities().getCapability("deviceName"));
                    String os_version = String.valueOf(getAppiumDriverThreadLocal().getCapabilities().getCapability("os_version"));
                    ExtentTest extentTest = extentReports.createTest(deviceName + " v" + os_version + ": " + testScenarioName);
                    extentTestThreadLocalTestCase.set(extentTest);
                } else {
                    // android
                    if (Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("Cloud").equalsIgnoreCase("true")) {
                        // cloud android
                        String deviceName = String.valueOf(getAppiumDriverThreadLocal().getCapabilities().getCapability("device"));
                        String os_version = String.valueOf(getAppiumDriverThreadLocal().getCapabilities().getCapability("platformVersion"));
                        ExtentTest extentTest = extentReports.createTest(deviceName + " v" + os_version + ": " + testScenarioName);
                        extentTestThreadLocalTestCase.set(extentTest);
                    } else {
                        // local android
                        String deviceName = String.valueOf(getAppiumDriverThreadLocal().getCapabilities().getCapability("deviceModel"));
                        String os_version = String.valueOf(getAppiumDriverThreadLocal().getCapabilities().getCapability("platformVersion"));
                        ExtentTest extentTest = extentReports.createTest(deviceName + " v" + os_version + ": " + testScenarioName);
                        extentTestThreadLocalTestCase.set(extentTest);
                    }
                }
            } else if (webCloud) {
                String browserName = String.valueOf(getRemoteWebDriverThreadLocal().getCapabilities().getCapability("browserName"));
                String platform = String.valueOf(getRemoteWebDriverThreadLocal().getCapabilities().getCapability("platform"));
                ExtentTest extentTest = extentReports.createTest(platform + " " + browserName + ": " + testScenarioName);
                extentTestThreadLocalTestCase.set(extentTest);
            } else {
                String browserName = getWebDriverThreadLocal().getClass().getSimpleName().substring(0, 6);
                ExtentTest extentTest = extentReports.createTest(browserName + ": " + testScenarioName);
                extentTestThreadLocalTestCase.set(extentTest);
            }
        }
    };
    private EventHandler<TestStepStarted> eventHandlerTestStepStarted = new EventHandler<TestStepStarted>() {
        public void receive(TestStepStarted event) {
            if (event.getTestStep() instanceof PickleStepTestStep) {
                String testStepName = ((PickleStepTestStep) event.getTestStep()).getStep().getText();
                ExtentTest extentTest = extentTestThreadLocalTestCase.get().createNode(testStepName);
                extentTestThreadLocalTestStep.set(extentTest);
            }
        }
    };
    private EventHandler<TestStepFinished> eventHandlerTestStepFinished = new EventHandler<TestStepFinished>() {
        public void receive(TestStepFinished event) {
            if (event.getTestStep() instanceof PickleStepTestStep) {
                if (event.getResult().getStatus().toString().equalsIgnoreCase("passed")) {
                    if (webCloud) {
                        extentTestThreadLocalTestStep.get().log(Status.INFO, "Screenshot", MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshotAsBase64WebCloud()).build());
                    } else if (webLocal) {
                        extentTestThreadLocalTestStep.get().log(Status.INFO, "Screenshot", MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshotAsBase64WebLocal()).build());
                    } else if (mobile) {
                        extentTestThreadLocalTestStep.get().log(Status.INFO, "Screenshot", MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshotAsBase64Mobile()).build());
                    }
                    extentTestThreadLocalTestStep.get().pass("Test passed");
                } else if (event.getResult().getStatus().toString().equalsIgnoreCase("failed")) {
                    if (webCloud) {
                        extentTestThreadLocalTestStep.get().log(Status.INFO, "Screenshot", MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshotAsBase64WebCloud()).build());
                    } else if (webLocal) {
                        extentTestThreadLocalTestStep.get().log(Status.INFO, "Screenshot", MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshotAsBase64WebLocal()).build());
                    } else if (mobile) {
                        extentTestThreadLocalTestStep.get().log(Status.INFO, "Screenshot", MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshotAsBase64Mobile()).build());
                    }
                    extentTestThreadLocalTestStep.get().fail("Test failed: " + event.getResult().getError());
                } else {
                    extentTestThreadLocalTestStep.get().skip("Test skipped");
                }
            }
        }
    };
    public EventHandler<TestCaseFinished> eventHandlerTestCaseFinished = new EventHandler<TestCaseFinished>() {
        public void receive(TestCaseFinished event) {
            extentReports.flush();
/*    Below code is to update test status in jira
      String testScenarioKey = event.getTestCase().getTags().get(1).substring(9);
      String issueIDOfScenario = "get issue id using above scenario key and GetScenarioIssueIDsUsingKeyFromJiraAPI.java class";
      int testResult;
      if(event.getResult().getStatus().toString().equalsIgnoreCase("PASSED")){
        testResult=1;
        String executionID = "get execution id from AddZephyrTestsToTestCycleAndGetExecutionID.java class" +
                "by providing issueIDOfScenario and cycleID";
        Now execute the test by calling ExecuteTestsinTestCycleInZephyrSquadCloud.java class
      }
      if(event.getResult().getStatus().toString().equalsIgnoreCase("FAILED")){
        testResult=2;
        String executionID = "get execution id from AddZephyrTestsToTestCycleAndGetExecutionID.java class" +
                "by providing issueIDOfScenario and cycleID";
        Now execute the test by calling ExecuteTestsinTestCycleInZephyrSquadCloud.java class
      }
*/
        }
    };
    private EventHandler<TestRunFinished> eventHandlerTestRunFinished = new EventHandler<TestRunFinished>() {
        public void receive(TestRunFinished event) {
        }
    };
}
/*
https://www.javadoc.io/doc/io.cucumber/cucumber-core/4.7.2/cucumber/api/event/Event.html
*/