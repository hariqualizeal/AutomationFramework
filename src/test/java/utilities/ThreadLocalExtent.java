package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.Platform;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreadLocalExtent {
  private static ExtentReports extent;
  private static Platform platform;
  private static String reportFileName = "ExtentReport";
  private static String macPath = System.getProperty("user.dir") + "/TestReport";
  private static String windowsPath = System.getProperty("user.dir") + "\\target\\extent-reports";
  private static String macReportFileLoc = macPath + "/" + reportFileName;
  private static String winReportFileLoc = windowsPath + "\\" + reportFileName + "_" + getCurrentDateAndTime() + ".html";

  public static ExtentReports getInstance() {
    if (extent == null)
      createInstance();
    return extent;
  }

  public static String getCurrentDateAndTime() {
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
    Date date = new Date();
    String str = dateFormat.format(date).replace(" ", "_").replaceAll("/", "_").replaceAll(":", "_");
    return str;
  }

  //Create an extent report instance
  public static ExtentReports createInstance() {
    platform = getCurrentPlatform();
    String fileName = getReportFileLocation(platform);
    ExtentSparkReporter htmlReporter = new ExtentSparkReporter(fileName);
    htmlReporter.config().setTheme(Theme.STANDARD);
    htmlReporter.config().setDocumentTitle(fileName);
    htmlReporter.config().setEncoding("utf-8");
    htmlReporter.config().setReportName(fileName);
    extent = new ExtentReports();
    extent.attachReporter(htmlReporter);
    return extent;
  }

  //Select the extent report file location based on platform
  private static String getReportFileLocation(Platform platform) {
    String reportFileLocation = null;
    switch (platform) {
      case MAC:
        reportFileLocation = macReportFileLoc;
        createReportPath(macPath);
        System.out.println("ExtentReport Path for MAC: " + macPath + "\n");
        break;
      case WINDOWS:
        reportFileLocation = winReportFileLoc;
        createReportPath(windowsPath);
        System.out.println("ExtentReport Path for WINDOWS: " + windowsPath + "\n");
        break;
      default:
        System.out.println("ExtentReport path has not been set! There is a problem!\n");
        break;
    }
    return reportFileLocation;
  }

  //Create the report path if it does not exist
  private static void createReportPath(String path) {
    File testDirectory = new File(path);
    if (!testDirectory.exists()) {
      if (testDirectory.mkdir()) {
        System.out.println("Directory: " + path + " is created!");
      } else {
        System.out.println("Failed to create directory: " + path);
      }
    } else {
      System.out.println("Directory already exists: " + path);
    }
  }

  //Get current platform
  private static Platform getCurrentPlatform() {
    if (platform == null) {
      String operSys = System.getProperty("os.name").toLowerCase();
      if (operSys.contains("win")) {
        platform = Platform.WINDOWS;
      } else if (operSys.contains("nix") || operSys.contains("nux")
              || operSys.contains("aix")) {
        platform = Platform.LINUX;
      } else if (operSys.contains("mac")) {
        platform = Platform.MAC;
      }
    }
    return platform;
  }
}
