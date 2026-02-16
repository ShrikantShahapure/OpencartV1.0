package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBaseUtility.BaseClass;
import utilities.DataProviders;

public class TC003_LoginDDT extends BaseClass {

	// getting data provider different class (utility pkg )
	@Test(dataProvider = "LoginData", dataProviderClass = DataProviders.class, groups="Datadriven")
	public void verify_loginDDT(String email, String pwd, String exp) {

		logger.info("******* Started TC_003_LoginDDT ******");
      
		try {
		
		HomePage hp = new HomePage(driver);
		hp.clickMyAccount();
		hp.clickLogin();

		LoginPage lp = new LoginPage(driver);
		lp.setEmail(email);
		lp.setPassword(pwd);
		lp.clickLogin();

		MyAccountPage map = new MyAccountPage(driver);
		boolean targetPage = map.isMyAccountPageExist();

		if (exp.equalsIgnoreCase("Valid")) {

			if (targetPage == true) {
				
				map.clickLogout();
				Assert.assertTrue(true);
				
			} else {
				Assert.assertTrue(false);
			}
		}

		if (exp.equalsIgnoreCase("Invalid")) {

			if (targetPage == true) {

				
				map.clickLogout();
				Assert.assertTrue(false);
				
			} else {
				Assert.assertTrue(true);
			}
		}

		} catch(Exception e) {
			logger.error("Test Failed due to Exception :" + e);
			Assert.fail();
		}
		
		logger.info("******* Finished TC_003_LoginDDT ******");
		
		/*
		 * 1) Data is Valid => Login Success => Test Pass => Logout => Login Failed => Test
		 * Failed
		 * 
		 * 2) Data is InValid => Login Success => Test fail => Logout => Login Failed =>
		 * Test Pass
		 */

	}

}
