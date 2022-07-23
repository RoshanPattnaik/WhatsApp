package com.whatsapp;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.Hashtable;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.whatsapp.utilities.ReadExcel;
import com.whatsapp.utilities.Reports;
import com.aventstack.extentreports.Status;

public class MessageTest {
	WebDriver driver;
	ExtentReports reports;
	ExtentTest test;

	@BeforeTest
	public void initBrowser() {
		reports = Reports.getReports();
		test = reports.createTest("Send Automated Text Messages To WhatsApp Contacts");
		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir") + "\\src\\test\\resources\\chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("debuggerAddress", "localhost:3333");

		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}


	@Test(dataProvider = "fetchData")
	public void wamessageTest(Hashtable<String, String> data) {
		
		try {
			test.info("Searching... "+ data.get("Contact Name"));
			driver.findElement(By.xpath("//div[@title='Search input textbox']")).clear();
			driver.findElement(By.xpath("//div[@title='Search input textbox']")).sendKeys(data.get("Contact Name"));
			String searchResult = "//span[text()='" + data.get("Contact Name") + "']";
			driver.findElement(By.xpath(searchResult)).click();
			test.info("Selected..."+ data.get("Contact Name"));
			driver.findElement(By.xpath("//div[@title='Type a message']")).sendKeys(data.get("Message"));
			driver.findElement(By.xpath("//span[@data-testid='send']")).click();
			test.pass("Message was successfully sent to "+ data.get("Contact Name"));
		} catch (Exception e) {
			test.fail("Message Could not be sent to "+ data.get("Contact Name"));
		}
		test.pass("Completed!!!!");
	}

	@DataProvider
	public Object[][] fetchData() {

		String sheetName = "WhatsApp";
		ReadExcel excel = new ReadExcel(
				System.getProperty("user.dir") + "//src//test//resources//WhatsApp_Messages.xlsx");

		int totalCols = 0;
		int colRowNo = 1;
		while (!excel.fetchCellData(sheetName, totalCols, colRowNo).equals("")) {
			totalCols++;
		}

		System.out.println("Total columns:" + totalCols);

		int dataRowNum = 2;
		int dataCount = 0;
		while (!excel.fetchCellData(sheetName, 0, dataRowNum + dataCount).equals("")) {
			dataCount++;
		}
		test.info("WhatsApp meesages would be sent to "+dataCount+" Contacts");

		Object[][] data = new Object[dataCount][1];
		Hashtable<String, String> table = null;
		int counter = 0;
		// extract data
		for (int row = dataRowNum; row < dataRowNum + dataCount; row++) {
			table = new Hashtable<String, String>();
			for (int col = 0; col < totalCols; col++) {
				String key = excel.fetchCellData(sheetName, col, colRowNo);
				String value = excel.fetchCellData(sheetName, col, row);
				System.out.print(key + "******" + value);
				table.put(key, value);
			}
			data[counter][0] = table;
			counter++;
			System.out.println();
		}
		return data;

	}

	@AfterMethod
	public void flush() {
		if (reports != null)
			reports.flush();
	}

}
