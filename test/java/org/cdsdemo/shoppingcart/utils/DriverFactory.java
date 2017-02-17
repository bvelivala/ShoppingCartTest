package org.cdsdemo.shoppingcart.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverFactory {
	public static WebDriver OpenBrowser(String browserName) {
		long startTime = System.currentTimeMillis();
		String OS = System.getProperty("os.name").toLowerCase();
		boolean isWinOS = OS.contains("win");
		String browserToOpen = browserName.replaceAll("\\[\\[.*\\]\\]", "");
		
		WebDriver webdriver = null;
		String PROXY = Utils.prop.getProperty("PROXY");
		Proxy proxy = new Proxy();
		proxy.setHttpProxy(PROXY).setFtpProxy(PROXY).setSslProxy(PROXY)
				.setSocksProxy(PROXY);
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability(CapabilityType.PROXY, proxy);
		
		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir") + "" + Utils.file_separator
						+ "libs" + Utils.file_separator + "WebDrivers"
						+ Utils.file_separator + "chromedriver"
						+ (isWinOS ? ".exe" : ""));
						
		System.setProperty("webdriver.ie.driver",
				System.getProperty("user.dir") + "" + Utils.file_separator
						+ "libs" + Utils.file_separator + "WebDrivers"
						+ Utils.file_separator + "IEDriverServer"
						+ (isWinOS ? ".exe" : ""));
		
		switch (browserToOpen) {
		case "ie":
			webdriver = new InternetExplorerDriver(cap);
			webdriver.manage().window().maximize();
			break;
		case "firefox":
			webdriver = new FirefoxDriver(cap);
			webdriver.manage().window().maximize();
			break;
		case "chrome":
			webdriver = new ChromeDriver(cap);
			webdriver.manage().window().maximize();
			break;		
		}
		long dur = System.currentTimeMillis() - startTime;
		System.out.println("Browser is: " + webdriver.toString());
		System.out.println("Opening browser " + browserToOpen + " took " + dur
				+ " ms");
		return webdriver;
	}

	public static void removeItemFromLocalStorage(WebDriver driver, String item) {
		((JavascriptExecutor) driver).executeScript(String.format(
				"window.localStorage.removeItem('%s');", item));
	}

	public static boolean isItemPresentInLocalStorage(WebDriver driver,
			String item) {
		return !(((JavascriptExecutor) driver).executeScript(String.format(
				"return window.localStorage.getItem('%s');", item)) == null);
	}

	public static String getItemFromLocalStorage(WebDriver driver, String key) {
		return (String) ((JavascriptExecutor) driver).executeScript(String
				.format("return window.localStorage.getItem('%s');", key));
	}

	public static String getKeyFromLocalStorage(WebDriver driver, int key) {
		return (String) ((JavascriptExecutor) driver).executeScript(String
				.format("return window.localStorage.key('%s');", key));
	}

	public static Long getLocalStorageLength(WebDriver driver) {
		return (Long) ((JavascriptExecutor) driver)
				.executeScript("return window.localStorage.length;");
	}

	public static void setItemInLocalStorage(WebDriver driver, String item,
			String value) {
		((JavascriptExecutor) driver).executeScript(String.format(
				"window.localStorage.setItem('%s','%s');", item, value));
	}

	public static void clearLocalStorage(WebDriver driver) {
		((JavascriptExecutor) driver).executeScript(String
				.format("window.localStorage.clear();"));
	}

	public static void removeItemFromSessionStorage(WebDriver driver,
			String item) {
		((JavascriptExecutor) driver).executeScript(String.format(
				"window.sessionStorage.removeItem('%s');", item));
	}

	public static boolean isItemPresentInSessionStorage(WebDriver driver,
			String item) {
		return !(((JavascriptExecutor) driver).executeScript(String.format(
				"return window.sessionStorage.getItem('%s');", item)) == null);
	}

	public static String getItemFromSessionStorage(WebDriver driver, String key) {
		return (String) ((JavascriptExecutor) driver).executeScript(String
				.format("return window.sessionStorage.getItem('%s');", key));
	}

	public static String getKeyFromSessionStorage(WebDriver driver, int key) {
		return (String) ((JavascriptExecutor) driver).executeScript(String
				.format("return window.sessionStorage.key('%s');", key));
	}

	public static Long getSessionStorageLength(WebDriver driver) {
		return (Long) ((JavascriptExecutor) driver)
				.executeScript("return window.sessionStorage.length;");
	}

	public static void setItemInSessionStorage(WebDriver driver, String item,
			String value) {
		((JavascriptExecutor) driver).executeScript(String.format(
				"window.sessionStorage.setItem('%s','%s');", item, value));
	}

	public static void clearSessionStorage(WebDriver driver) {
		((JavascriptExecutor) driver).executeScript(String
				.format("window.sessionStorage.clear();"));
	}
}
