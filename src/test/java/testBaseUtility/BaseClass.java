package testBaseUtility;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseClass {

	static public WebDriver driver;
	public Logger logger; // log4j
	public Properties prop;

	@BeforeClass(groups = { "Sanity", "Regression", "Master" })
	@Parameters({ "os", "browser" })
	public void setup(String os, String browser) throws IOException {

		// Loading config.properties file
		FileReader file = new FileReader("./src//test//resources//config.properties");
		prop = new Properties();
		prop.load(file);

		logger = LogManager.getLogger(this.getClass());

		if (prop.getProperty("execution_env").equalsIgnoreCase("remote")) {

			// ========== REMOTE EXECUTION (Selenium Grid) ==========
			DesiredCapabilities capablity = new DesiredCapabilities();

			// set OS
			if (os.equalsIgnoreCase("windows")) {
				capablity.setPlatform(Platform.WIN10);
			} else if (os.equalsIgnoreCase("mac")) {
				capablity.setPlatform(Platform.MAC);
			} else if (os.equalsIgnoreCase("linux")) {
				capablity.setPlatform(Platform.LINUX);
			} else {
				System.out.println("No Matching OS");
				return;
			}

			// set browser
			switch (browser.toLowerCase()) {
			case "chrome":
				capablity.setBrowserName("chrome");
				break;
			case "edge":
				capablity.setBrowserName("MicrosoftEdge");
				break;
			case "firefox":
				capablity.setBrowserName("firefox");
				break;
			default:
				System.out.println("No browser Matching");
				return;
			}

			// Create RemoteWebDriver
			driver = new RemoteWebDriver(new URL("http://192.168.43.65:4444/wd/hub"), capablity);
		} else {

			// ========== LOCAL EXECUTION ==========
			switch (browser.toLowerCase()) {
			case "chrome":
				System.setProperty("webdriver.chrome.driver", "./drivers\\chromedriver.exe");
				driver = new ChromeDriver();
				break;
			case "edge":
				System.setProperty("webdriver.edge.driver", "./drivers\\msedgedriver.exe");
				driver = new EdgeDriver();
				break;

			default:
				throw new RuntimeException("Invalid borwser name :" + browser);
			}
		}

		// Common settings for both local and remote
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		driver.get(prop.getProperty("appURL")); // reading url from properties file
		driver.manage().window().maximize();
	}

	@AfterClass(groups = { "Sanity", "Regression", "Master" })
	public void tearDown() {
		driver.quit();
	}

	// This 3 methods generating new String and Number Every time
	// ( its not directly from java its from common library )

	public String genNewString() {
		String generatedString = RandomStringUtils.randomAlphabetic(7);
		return generatedString;
	}

	public String genNewNumber() {
		String generatedNumber = RandomStringUtils.randomNumeric(10);
		return generatedNumber;
	}

	public String genNewAlphaNumric() {
		String generatedString = RandomStringUtils.randomAlphabetic(3);
		String generatedNumber = RandomStringUtils.randomNumeric(3);
		return (generatedString + generatedNumber);
	}

	public String captureScrrenshot(String tname) {

		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

		TakesScreenshot takesScreenShot = (TakesScreenshot) driver;
		File sourceFile = takesScreenShot.getScreenshotAs(OutputType.FILE);

		String targetPathFile = System.getProperty("user.dir") + "\\screenshots\\" + tname + "" + timeStamp + ".png";
		File targetFile = new File(targetPathFile);

		sourceFile.renameTo(targetFile);

		return targetPathFile;
	}

}

// Automatically handles ChromeDriver version
//WebDriverManager.chromedriver().setup();
//driver = new ChromeDriver();
