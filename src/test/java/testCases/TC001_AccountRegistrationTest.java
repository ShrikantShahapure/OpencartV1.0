package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import testBaseUtility.BaseClass;

public class TC001_AccountRegistrationTest extends BaseClass {

	@Test(groups= {"Regression", "Master" })
	public void verify_account_registration() {

		logger.info("***** Starting TC001_AccountRegistrationTest ***");

		try {
			HomePage hp = new HomePage(driver);
			hp.clickMyAccount();
			logger.info("Clicked on My Account Link");
			hp.clickRegister();
			logger.info("Clicked on Register Link");

			logger.info("Providing customer details");
			AccountRegistrationPage regpage = new AccountRegistrationPage(driver);
			regpage.setFirstName(genNewString().toUpperCase());
			regpage.setLastName(genNewString().toUpperCase());

			regpage.setEmail(genNewString() + "@gmail.com");
			regpage.setTelephone(genNewNumber());

			String password = genNewAlphaNumric();

			regpage.setPassword(password);
			regpage.setConfirmPassword(password);

			regpage.setPrivacyPolicy();
			regpage.clickContinue();

			logger.info("Validating expected messege");
			String confmsg = regpage.getConfirmationMsg();

			Assert.assertTrue(confmsg.contains("Your Account Has Been Created!"),
				"Account Registration failed"
					);  
			
			
		} catch (Exception e) {
            logger.error("Test execution failed", e);
			Assert.fail("Excception Occured during test execution");
			logger.info("Launching browser: " +
		            " | Thread ID: " + Thread.currentThread().getId());

		}

		logger.info("***** Finished TC001_AccountRegistrationTest ***");

	}

}
