package com.core.ddf.Indee.testcases;

import java.util.Hashtable;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.core.ddf.Indee.base.BaseTest;
import com.core.ddf.Indee.DataUtil;
import com.core.ddf.Indee.Xls_Reader;
import com.relevantcodes.extentreports.LogStatus;

public class DemoScreener extends BaseTest{
	
	String testCaseName="DemoScreener";
	SoftAssert softAssert;
	Xls_Reader xls;
	
	@Test(dataProvider="getData")
	public void DemoScreenerTest(Hashtable<String,String> data){
		
		test = rep.startTest("DemoScreenerTest");
		test.log(LogStatus.INFO, data.toString());
		if(!DataUtil.isRunnable(testCaseName, xls) ||  data.get("Runmode").equalsIgnoreCase("N")){
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			throw new SkipException("Skipping the test as runmode is N");
		}
		openBrowser(data.get("Browser"));
		navigate("appurl");
		//demoScreener("ankur","ank.choudhary@gmail.com","5","6 hrs","","");
		
		demoScreener(data.get("username"),data.get("email"),data.get("maxViews"),data.get("perishesIn"),data.get("Trailer"),data.get("Feature"));
		reportPass("demo Test Passed");
	}
	
	
	@BeforeMethod
	public void init(){
		softAssert = new SoftAssert();
		super.init();
	}
	
	
	@AfterMethod
	public void quit(){
		try{
			softAssert.assertAll();
		}catch(Error e){
			test.log(LogStatus.FAIL, e.getMessage());
		}
		if(rep!=null){
			rep.endTest(test);
			rep.flush();
		}
		
		    if(driver!=null)
			driver.quit();
	}
	
	@DataProvider
	public Object[][] getData(){		
		super.init();	
		xls = new Xls_Reader(prop.getProperty("xlspath"));
		return DataUtil.getTestData(xls, testCaseName);
		
	}


}
