package org.cdsdemo.shoppingcart.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import com.google.common.base.Predicate;

public class Utils {
	public static String file_separator = System.getProperty("file.separator");

	public static String projectFolder = System.getProperty("user.dir",
			"not found");
	public static Properties prop = new Properties();

	/**
	 * Returns the verify part of value string that'll be used in RunStep as
	 * value.
	 * 
	 * @param val
	 *            - String used as input value for RunStep.
	 * @return Returns the verify part of val string.
	 */
	public static String getVerifyVal(String val) {
		return getVal(val, 0);
	}

	/**
	 * Returns the verify part of value string that'll be used in RunStep as
	 * value.
	 * 
	 * @param val
	 *            - String used as input value for RunStep.
	 * @return Returns the verify part of val string.
	 */
	public static String getEnterVal(String val) {
		return getVal(val, 1);
	}

	/**
	 * Returns the verify part of value string that'll be used in RunStep as
	 * value.
	 * 
	 * @param val
	 *            - String used as input value for RunStep.
	 * @return Returns the verify part of val string.
	 */
	public static String getPostVerifyVal(String val) {
		return getVal(val, 2);
	}

	/**
	 * Helper function for getVerifyVal and getEnterVal
	 * 
	 * @param val
	 *            - The value
	 * @param num
	 *            - 0 for getVerifyVal and 1 for getEnterVal
	 * @return the value to verify or enter
	 * @see getVerifyVal
	 * @see getEnterVal
	 */
	private static String getVal(String val, int num) {
		String[] valSplit = val.split("[|]", -1);
		String verifyVal;
		String enterVal;
		String postVerifyVal;

		// System.out.println("valSplit length is: " + valSplit.length);
		switch (valSplit.length) {
		case 1:
			verifyVal = "";
			enterVal = val;
			postVerifyVal = "";
			break;
		case 2:
			verifyVal = valSplit[0];
			enterVal = valSplit[1];
			postVerifyVal = "";
			break;
		case 3:
			verifyVal = valSplit[0];
			enterVal = valSplit[1];
			postVerifyVal = valSplit[2];
			break;
		default:
			System.out.println("unknown valsplit length");
			return "";
		}

		switch (num) {
		case 0:
			return verifyVal;
		case 1:
			return enterVal;
		case 2:
			return postVerifyVal;
		default:
			return "";
		}
	}

	public static void appendToFile(String filePath, String text) {

		BufferedWriter bw = null;

		try {
			// create file
			File outputFile = new File(filePath);
			outputFile.createNewFile(); // if file already exists will do
										// nothing
			// APPEND MODE SET HERE
			bw = new BufferedWriter(new FileWriter(filePath, true));
			bw.write(text);
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally { // always close the file
			if (bw != null)
				try {
					bw.close();
				} catch (IOException ioe2) {
					// just ignore it
				}
		} // end try/catch/finally
	}

	public static String getKeyword(String key, String defaultval) {
		String valfound = getKeyword(key);		
		String retval = valfound.isEmpty() ? defaultval : valfound;		
		return retval;
	}

	public static String getKeyword(String val) {

		String val2 = val.replaceAll("[{}]", "").trim();
		// System.out.println("Look for " + val2);
		val2 = getLineFromFile(
				val2,
				projectFolder + file_separator + "TestObjects" + file_separator
						+ "Keywords.txt").trim();
		// System.out.println("found " + val2);
		if (val2.isEmpty())
			return val2;
		val2 = val2.split("[=]")[1].trim();
		switch (val2) {
		case "DATE":
			val2 = new SimpleDateFormat("dd/MM/yyyy").format(new Date(System
					.currentTimeMillis()));
			break;
		case "DATETIME":
			val2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(
					System.currentTimeMillis()));
			break;
		default:
			// nothing
		}
		return val2;
	}

	public static String getLineFromFile(String linePart, String propFile) {
		BufferedReader br = null;
		String line = "";
		Boolean fileFound = false;
		Boolean objFound = false;
		try {
			// br = new BufferedReader(new FileReader(propFile));
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					propFile), "ISO-8859-1"));
			fileFound = true;
			while ((line = br.readLine()) != null) {
				if (line.contains("***")) {
					line = "";
					break;
				}

				if (line.trim().isEmpty())
					continue;
				if (line.contains(linePart)) {
					objFound = true;
					break;
				}
			}
		} catch (IOException e) {
			// System.out.println("File " + propFile + " not found");
			line = "";
		} finally {
			try {
				br.close();
			} catch (IOException e) {
			}
		}

		if (fileFound && !objFound) {
			// System.out
			// .println("Line " + linePart + " not found in " + propFile);
			line = "";
		}
		return line;
	}

	public static By getObjByDesc(String desc, int i) {
		String[] descparts = desc.split(":=");

		// System.out.println("desc is: " + desc + " of length "
		// + descparts.length);
		String by = descparts[0];
		String locator = descparts[1];
		String by2 = "";
		String locator2 = "";

		By testObj = null;
		switch (by) {
		case "id":
			testObj = By.id(locator);
			break;
		case "css":
			testObj = By.cssSelector(locator);
			break;
		case "xpath":
			testObj = By.xpath(locator);
			break;
		default:
			System.out.println("Unknown By type " + by);
			break;
		}

		By testObj2 = null;
		if (descparts.length > 2) {
			by2 = descparts[2];
			locator2 = descparts[3];
			switch (by2) {
			case "id":
				testObj2 = By.id(locator2);
				break;
			case "css":
				testObj2 = By.cssSelector(locator2);
				break;
			case "xpath":
				testObj2 = By.xpath(locator2);
				break;
			default:
				System.out.println("Unknown By2 type " + by2);
				break;
			}
		}
		if (i == 0) {
			return testObj;
		}
		return testObj2;
	}


	public static void saveScreenshot(WebDriver driver, String str) {
		String fileName = str.replaceAll("[^a-zA-Z0-9]", "_");
		int fileNameLen = fileName.length();

		int maxFileNameLen = 80;
		if (fileNameLen > maxFileNameLen)
			fileNameLen = maxFileNameLen;
		fileName = fileName.substring(0, fileNameLen);
		// Ready the folder

		// Ready the screenshot file
		Boolean sshotFileExists = true;
		String sshotFileName = projectFolder + "" + Utils.file_separator
				+ "test-output" + Utils.file_separator + "screenshots"
				+ Utils.file_separator + "" + fileName + "_#IDX.png";
		int idx = 0;
		File file = null;
		while (sshotFileExists) {
			idx++;
			file = new File(sshotFileName.replace("#IDX", "" + idx));
			sshotFileExists = file.isFile();
		}
		// Take screenshot and store as a file format
		File src = null;
		try {
			src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		} catch (Exception e) {
			System.out.println("Unable to screenshot " + str);
		}
		try {
			FileUtils.copyFile(src,
					new File(sshotFileName.replace("#IDX", "" + idx)));
		} catch (IOException | NullPointerException e) {
			System.out.println("unable to save screenshot "
					+ sshotFileName.replace("#IDX", "" + idx));
		}
	}

	/**
	 * Clicks the element ordinarily. If it fails, tries to move mouse to it and
	 * click. If it fails again, uses JavaScript to click.
	 * 
	 * @param elem
	 *            the element to click.
	 */
	public static void forceClick(WebElement elem, WebDriver driver) {
		Boolean clickDone = false;
		// Ordinary click
		try {
			new WebDriverWait(driver, 5).until(ExpectedConditions
					.elementToBeClickable(elem));
			elem.click();
			// System.out.println("\t\t\t" + fName + " Ordinary click");
			clickDone = true;
		} catch (Exception e) {
			// System.out.println("\t\t\t" + fName
			// + " Ordinary click exception, clickDone " + clickDone);
		}
		if (clickDone)
			return;

		// JavaScript click
		try {
			((JavascriptExecutor) driver).executeScript(
			// "arguments[0].scrollIntoView(true);" +
					"arguments[0].click()", elem);
		} catch (StaleElementReferenceException e) {
			System.out.println("rare stale ref exception at forceclick");
		}
		// System.out.println("\t\t\t" + fName + " JavaScript click");
	}

	public static Iterator<Object[]> getTestData(String csvFile) {
		List<Object[]> testCases = new ArrayList<>();
		String[] data = null;
		String cvsSplitBy = ",";
		BufferedReader br = null;
		String line = "";
		int rowsAdded = 0;
		int maxRows = 2000;
		try {
			maxRows = Integer.parseInt(prop.getProperty("MAX_ROWS", "1000"));
		} catch (NumberFormatException e) {
			System.out.println("MAX_ROWS invalid, using value " + maxRows);
		}
		System.out.println("MAX_ROWS is " + maxRows + " (" + csvFile + ")");
		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				if (line.contains("***")) {
					// end of test data, exit loop and return value
					break;
				}
				if (line.contains("//skip") || line.contains("////")) {
					// skip this test data
					continue;
				}
				// use comma as separator
				data = line.split(cvsSplitBy);
				for (int i = 0; i < data.length; i++) {
					String col = data[i];
					col = col.replace("#COMMA#", ",");
					col = col.trim();
					data[i] = col;
				}
				if (rowsAdded < maxRows)
					testCases.add(data);

				rowsAdded++;
			}
		} catch (IOException e) {
			System.out.println("File " + csvFile + "not found");
		} finally {
			try {
				br.close();
			} catch (IOException e) {
			}
		}
		return testCases.iterator();

	}

	public static void waitForPageLoadedAnimCompleted(WebDriver driver) {
		try {

			new WebDriverWait(driver, 15).until(ExpectedConditions
					.elementToBeClickable(By.id("logo")));
		} catch (TimeoutException e) {
			System.out.println("Page loading logo to be clickable timed out");
		}

	}

	static void waitForPageLoad(WebDriver wdriver) {
		WebDriverWait wait = new WebDriverWait(wdriver, 60);

		Predicate<WebDriver> pageLoaded = new Predicate<WebDriver>() {

			@Override
			public boolean apply(WebDriver input) {
				return ((JavascriptExecutor) input).executeScript(
						"return document.readyState").equals("complete");
			}

		};
		wait.until(pageLoaded);
	}

	public static void gotoLatestBrowserTab(WebDriver driver) {
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
			System.out.println("Switched to tab " + driver.getTitle());
		}
	}

	
	/**
	 * Returns if the calling method is an @Test testng test or not.
	 * 
	 * @return true or false.
	 */
	public static Boolean isIndividualTest() {
		return (Thread.currentThread().getStackTrace()[3].toString()
				.contains("invoke0"));
	}

	public static void setupProp() {
		prop = new Properties();

		String propFile = Utils.projectFolder + Utils.file_separator
				+ "ShoppingCartTesting.properties";
		System.out.println("Properties file path = "+propFile);
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(propFile);
		} catch (FileNotFoundException e) {
			System.out.println("File" + propFile + " not found");
			e.printStackTrace();
		}

		try {
			prop.load(fileInput);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public static void setupFolders() {
		File testoutFolder = new File(projectFolder + file_separator
				+ "test-output");
		File screenshotFolder = new File(Utils.projectFolder + file_separator
				+ "test-output" + file_separator + "screenshots");

		try {
			testoutFolder.mkdir();
			screenshotFolder.mkdir();
			purgeDirectory(screenshotFolder);
		} catch (Exception e) {
		}
	}

	public static void printProperties() {
		System.out.println("Properties found: ");
		try {
			String[] props = prop.toString().split("[,]");
			for (String prop : props)
				System.out.println(prop);
		} catch (Exception e) {
			System.out.println("Properties reading exception");
		}
	}

	static void purgeDirectory(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory())
				purgeDirectory(file);
			file.delete();
		}
	}
}
