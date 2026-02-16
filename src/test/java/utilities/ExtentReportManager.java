package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testBaseUtility.BaseClass;

public class ExtentReportManager implements ITestListener {

	public ExtentSparkReporter sparkReporter;
	public ExtentReports extent;
	public ExtentTest test;

	String repName;

	public void onStart(ITestContext testContext) {

		// Create reports directory if it doesn't exist
		File reportsDir = new File(System.getProperty("user.dir") + "/reports");
		if (!reportsDir.exists()) {
			reportsDir.mkdirs();
		}

		// Generate timestamp-based report name
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		repName = "Test-Report-" + timeStamp + ".html";

		// ONLY ONE sparkReporter initialization (use consistent path format)
		String reportPath = System.getProperty("user.dir") + "/reports/" + repName;
		sparkReporter = new ExtentSparkReporter(reportPath);

		// Configure report
		sparkReporter.config().setDocumentTitle("Opencart Automation Report");
		sparkReporter.config().setReportName("Opencart Functional Testing");
		sparkReporter.config().setTheme(Theme.DARK);

		// Initialize ExtentReports
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);

		// System Information
		extent.setSystemInfo("Application", "Opencart");
		extent.setSystemInfo("Module", "Admin");
		extent.setSystemInfo("Sub Module", "Customers");
		extent.setSystemInfo("Username", System.getProperty("user.name"));
		extent.setSystemInfo("Environment", "QA");

		String os = testContext.getCurrentXmlTest().getParameter("os");
		extent.setSystemInfo("Operating System", os);

		String browser = testContext.getCurrentXmlTest().getParameter("browser");
		extent.setSystemInfo("Browser", browser);

		List<String> includeGroups = testContext.getCurrentXmlTest().getIncludedGroups();
		if (!includeGroups.isEmpty()) {
			extent.setSystemInfo("Groups", includeGroups.toString());
		}
	}

	public void onTestSuccess(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.PASS, result.getName() + " got successfully executed");
	}

	public void onTestFailure(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.FAIL, "Test case FAILED: " + result.getName());
		test.log(Status.FAIL, "Failure reason: " + result.getThrowable().getMessage());

		try {
			String imgPath = new BaseClass().captureScrrenshot(result.getName());
			test.addScreenCaptureFromPath(imgPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onTestSkipped(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.SKIP, "Test case SKIPPED: " + result.getName());
		test.log(Status.INFO, result.getThrowable().getMessage());
	}

	public void onFinish(ITestContext context) {
		// Flush report FIRST (creates the file)
		extent.flush();

		// Then open it
		String reportPath = System.getProperty("user.dir") + "/reports/" + repName;
		File extentReport = new File(reportPath);

		if (extentReport.exists()) {
			try {
				Desktop.getDesktop().browse(extentReport.toURI());
			} catch (IOException e) {
				System.err.println("Failed to open report: " + e.getMessage());
				System.out.println("Report saved at: " + reportPath);
			}
		} else {
			System.err.println("Report file not found: " + reportPath);
		}
	}
}