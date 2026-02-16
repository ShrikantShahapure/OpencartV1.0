package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBaseUtility.BaseClass;

public class TC002_LoginTest extends BaseClass {

	@Test(groups= {"Sanity", "Master"} )
	public void verify_login() {
		
		logger.info("***** Starting TC002_LoginTest *****");
		
		try {
		// HomePage
		HomePage hp = new HomePage(driver);
		hp.clickMyAccount();
		hp.clickLogin();
		
		// LoginPage
		LoginPage lp = new LoginPage(driver);
		lp.setEmail(prop.getProperty("email"));
		lp.setPassword(prop.getProperty("password"));
		lp.clickLogin();
		
		// MyAccountPage
		MyAccountPage map = new MyAccountPage(driver);
		boolean targetPage = map.isMyAccountPageExist();
		
		Assert.assertTrue(targetPage); // or Assert.assertEquals(targetPage, true, "Login failed");
		} 
		catch(Exception e) {
			Assert.fail();
		}
		
		logger.info("***** Finished TC002_LoginTest *****");
		
	}
	
}
