package org.cdsdemo.shoppingcart.regression;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class HomePageTest {
	
	public RemoteWebDriver driver;
	
	@Parameters({ "remoteDriverUrl" })
	@BeforeClass
	public void setUp(String remoteDriverURL) throws MalformedURLException {
		System.out.println("Remote Driver URL :: "+remoteDriverURL);
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		driver = new RemoteWebDriver(new URL(remoteDriverURL), capabilities);
		driver.manage().window().maximize();
	}
	
	@Parameters({ "appUrl" })
	@Test
	public void testHomePageTitle(String appURL) {
		System.out.println("*** Navigation to Application ***"+appURL);
		driver.navigate().to(appURL);
		String strPageTitle = driver.getTitle();
		System.out.println("*** Verifying page title ***");
		Assert.assertTrue(strPageTitle.equalsIgnoreCase("Shopping Online Sample"), "Page title doesn't match");
	}
	
	@AfterClass
	public void closeBrowser() {
		if (driver != null) {
			driver.quit();
		}
	}
}
