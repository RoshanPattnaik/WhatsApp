package com.whatsapp.utilities;

import java.io.File;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class Reports {
	
	
	static ExtentReports reports;
	public static String screenshotFolderPath;
	
	public static ExtentReports getReports() {
		if(reports == null) {
			reports = new ExtentReports();

			Date d = new Date();
			System.out.println(d.toString().replaceAll(":", "-"));
			String reportsFolder=d.toString().replaceAll(":", "-") +"//screenshots";
			
		    screenshotFolderPath = System.getProperty("user.dir") +"//reports//"+reportsFolder;
			String reportFolderPath = System.getProperty("user.dir") +"//reports//"+d.toString().replaceAll(":", "-");
			System.out.println(screenshotFolderPath);
			File f = new File(screenshotFolderPath);
			f.mkdirs();
			
			ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFolderPath);
			sparkReporter.config().setReportName("Production Regression Testing");
			sparkReporter.config().setDocumentTitle("Automation Reports");
			sparkReporter.config().setTheme(Theme.STANDARD);
			sparkReporter.config().setEncoding("utf-8");
			
			reports.attachReporter(sparkReporter);
		}
		
		return reports;
		
	}

}
