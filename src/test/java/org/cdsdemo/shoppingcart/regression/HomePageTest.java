package org.cdsdemo.shoppingcart.regression;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import org.cdsdemo.shoppingcart.utils.DriverFactory;
import org.cdsdemo.shoppingcart.utils.Utils;

public class HomePageTest {
	String appUrl;
	long startTime;
	WebDriver driver;

	@BeforeTest
	public void setUp() {
		String OS = System.getProperty("os.name").toLowerCase();
		System.out.println("Running in OS: " + OS);
		Utils.setupProp();
		String host = "unknown";
		try {
			host = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
		}
		System.out.println("System is: " + host);
		appUrl = Utils.prop.getProperty("appUrl");
		System.out.println("appUrl = "+appUrl);
		if (null==appUrl) {
			appUrl = "https://www.google.co.in";
		}
		driver = DriverFactory.OpenBrowser(Utils.prop.getProperty("Browser"));
	}

	@AfterTest
	public void tearDown() {
		driver.quit();
	}

	@BeforeMethod
	public void beforeMethod(Method m) {
		startTime = System.currentTimeMillis();
		System.out.println("Running method: " + m.getName());
	}

	@AfterMethod
	public void afterMethod(Method m) {
		long dur = System.currentTimeMillis() - startTime;
		System.out.println("Method " + m.getName() + " took " + dur + " ms");
	}

	@Test
	public void pingUrl() {
		String status = "Unknown";
		try {
			status = getStatus(appUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Ping Status:" + status);
	}

	@Test
	public void openUrlUsingGet() {

		driver.get(appUrl);
	}

	@Test
	public void openUrlUsingNavigateTo() {
		driver.navigate().to(appUrl);
	}

	@Test
	public void clickButton(String attr) {
		driver.get(appUrl);
		WebElement getQuoteBtn = new WebDriverWait(driver, 3)
				.until(ExpectedConditions.elementToBeClickable(By
						.id("a_otp_getquote")));
		String btnVal = getQuoteBtn.getAttribute(attr);
		Assert.assertEquals(btnVal, "GET QUOTE");
		System.out.println("Attr " + attr + " is " + btnVal);
	}

	public static String getStatus(String url) throws IOException {

		String result = "";
		try {
			URL siteURL = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) siteURL
					.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();

			int code = connection.getResponseCode();
			if (code == 200) {
				result = "Green";
			}
		} catch (Exception e) {
			result = "->Red<-";
		}
		return result;
	}	
}
