package com.core.ddf.Indee.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.core.ddf.Indee.ExtentManager;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class BaseTest {
	public WebDriver driver;
	public Properties prop;
	public Properties envProp;
	public ExtentReports rep = ExtentManager.getInstance();
	public ExtentTest test;
	//boolean gridRun=true;

	public void init(){
		//init the prop file
		
			prop=new Properties();
			//envProp=new Properties();
			try {
				FileInputStream fs = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//projectconfig.properties");
				prop.load(fs);
			//	String env=prop.getProperty("env");
			//	fs = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//"+env+".properties");
			//	envProp.load(fs);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	
	public void openBrowser(String bType){
		test.log(LogStatus.INFO, "Opening browser "+bType );
		
			if(bType.equals("Mozilla"))
				driver=new FirefoxDriver();
			else if(bType.equals("Chrome")){
				System.setProperty("webdriver.chrome.driver", prop.getProperty("chromedriver_exe"));
				driver=new ChromeDriver();
			}
			else if (bType.equals("IE")){
				System.setProperty("webdriver.chrome.driver", prop.getProperty("iedriver_exe"));
				driver= new InternetExplorerDriver();
			}
		
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		test.log(LogStatus.INFO, "Browser opened successfully "+bType);
		
		
	}

	public void navigate(String urlKey){
		test.log(LogStatus.INFO, "Navigating to "+prop.getProperty(urlKey) );
		System.out.println(prop.getProperty(urlKey));
		driver.get(prop.getProperty(urlKey));
	}
	
	public void click(String locatorKey){
		test.log(LogStatus.INFO, "Clicking on "+locatorKey);
		getElement(locatorKey).click();
		test.log(LogStatus.INFO, "Clicked successfully on "+locatorKey);

	}
	
	public void type(String locatorKey,String data){
		test.log(LogStatus.INFO, "Tying in "+locatorKey+". Data - "+data);
		getElement(locatorKey).sendKeys(data);
		test.log(LogStatus.INFO, "Typed successfully in "+locatorKey);

	}
	// finding element and returning it
	public WebElement getElement(String locatorKey){
		WebElement e=null;
		try{
		if(locatorKey.endsWith("_id"))
			e = driver.findElement(By.id(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_name"))
			e = driver.findElement(By.name(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_xpath"))
			e = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
		else{
			reportFailure("Locator not correct - " + locatorKey);
			Assert.fail("Locator not correct - " + locatorKey);
		}
		
		}catch(Exception ex){
			// fail the test and report the error
			reportFailure(ex.getMessage());
			ex.printStackTrace();
			Assert.fail("Failed the test - "+ex.getMessage());
		}
		return e;
	}
	/***********************Validations***************************/
	
	
	public boolean isElementPresent(String locatorKey){
		List<WebElement> elementList=null;
		if(locatorKey.endsWith("_id"))
			elementList = driver.findElements(By.id(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_name"))
			elementList = driver.findElements(By.name(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_xpath"))
			elementList = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
		else{
			reportFailure("Locator not correct - " + locatorKey);
			Assert.fail("Locator not correct - " + locatorKey);
		}
		
		if(elementList.size()==0)
			return false;	
		else
			return true;
	}
	
	public boolean verifyText(String locatorKey,String expectedTextKey){
		String actualText=getElement(locatorKey).getText().trim();
		String expectedText=prop.getProperty(expectedTextKey);
		if(actualText.equals(expectedText))
			return true;
		else 
			return false;
		
	}
	
	
	
	
	/*****************************Reporting********************************/
	
	public void reportPass(String msg){
		test.log(LogStatus.PASS, msg);
	}
	
	public void reportFailure(String msg){
		test.log(LogStatus.FAIL, msg);
		takeScreenShot();
		Assert.fail(msg);
	}
	
	public void takeScreenShot(){
		// fileName of the screenshot
		Date d=new Date();
		String screenshotFile=d.toString().replace(":", "_").replace(" ", "_")+".png";
		// store screenshot in that file
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir")+"//screenshots//"+screenshotFile));
		} catch (IOException e) {
			// TODO Auto-generated catcsh block
			e.printStackTrace();
		}
		//put screenshot file in reports
		test.log(LogStatus.INFO,"Screenshot-> "+ test.addScreenCapture(System.getProperty("user.dir")+"//screenshots//"+screenshotFile));
		
	}
	

	
	public void wait(int timeToWaitInSec){
		try {
			Thread.sleep(timeToWaitInSec * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getText(String locatorKey){
		test.log(LogStatus.INFO, "Getting text from "+locatorKey);
		return getElement(locatorKey).getText();

	}
	
	
	
	/************************App functions*****************************/
	
	public void demoScreener(String username,String email, String maxViews, String perishesIn, String Trailer, String Feature) {
		test.log(LogStatus.INFO, "Trying to send demo screener with username = "+ username);
		System.out.println("username="+email);
		
		type("name_xpath",username);
		type("email_xpath",email);
		//click("signinButton_xpath");
		List<WebElement> viewsList=driver.findElements(By.xpath(prop.getProperty("views_xpath")));
		List<WebElement> perishesList=driver.findElements(By.xpath(prop.getProperty("perishesin_xpath")));
		//System.out.println(viewsList.size());
		//System.out.println(perishesList.size());
		
		for(int i=0;i<viewsList.size();i++)
		{
			int j=i+1;
			//System.out.println(viewsList.get(i).getAttribute("value"));
			//System.out.println(maxViews);
			if (viewsList.get(i).getAttribute("value").toString().equalsIgnoreCase(maxViews))
			{
			
			driver.findElement(By.xpath(prop.getProperty("viewxpathpart1")+j+prop.getProperty("viewxpathpart2"))).click();
			break;
			}
			else if (j==viewsList.size()){
				reportFailure("views count does not exist");
				
			}
		}
		for(int i=0;i<perishesList.size();i++)
		{
			int j=i+1;
			//System.out.println(perishesList.get(i).getText());
			
			if (perishesList.get(i).getText().equalsIgnoreCase(perishesIn))
			{
			
			driver.findElement(By.xpath(prop.getProperty("perishesxpathpart1")+j+prop.getProperty("perishesxpathpart2"))).click();
			break;
			}else if (j==perishesList.size()){
				reportFailure("PerishesIn does not exist");
			}
		}
		
	//	if (Trailer.equalsIgnoreCase("checked"))
	//		click("checkbox1_xpath");
	//	if (Feature.equalsIgnoreCase("checked"))
	//		click("checkbox2_xpath");
		test.log(LogStatus.INFO, "Checking for video selected");
		if (Trailer.equalsIgnoreCase("checked"))
		{
			test.log(LogStatus.INFO, "Trailer is "+Trailer);
			String check=driver.findElement(By.xpath(prop.getProperty("checkbox1input_xpath"))).getAttribute("checked");
			System.out.println("check value for 1st"+check);
		//	if(!check.equals("true"))
		//	{click("checkbox1_xpath");}
		}
		
		else 
			{
			test.log(LogStatus.INFO, "Trailer is not checked");
			String check=driver.findElement(By.xpath(prop.getProperty("checkbox1input_xpath"))).getAttribute("checked");
			
			click("checkbox1_xpath");
			}
		if (Feature.equalsIgnoreCase("checked"))
		{
			test.log(LogStatus.INFO, "Feature is "+Feature);
			String check=driver.findElement(By.xpath(prop.getProperty("checkbox2input_xpath"))).getAttribute("checked");
			System.out.println("check value for 2nd"+check);
			if(check!="true")
			{click("checkbox2_xpath");}
		}
		
		else 
			{
			test.log(LogStatus.INFO, "Feature is not checked");
			String check=driver.findElement(By.xpath(prop.getProperty("checkbox2input_xpath"))).getAttribute("checked");
			if(check=="true")
			{click("checkbox2_xpath");}
			}
		 if (!Trailer.equalsIgnoreCase("checked") && !Feature.equalsIgnoreCase("checked"))
			{
			 test.log(LogStatus.INFO, "Both Trailer and Feature are not checked");
			 System.out.println("clicking on generate if Trailer!checked && Feature!=checked");
			 click("generate_xpath");
			 System.out.println("clicked on generate if Trailer!checked && Feature!=checked");
				wait(2);
				test.log(LogStatus.INFO, "Verifying the error message and refresh button");
				boolean error=isElementPresent("error_xpath");
				boolean refresh=isElementPresent("refresh_xpath");
				if (error==true && refresh==true){
					
					test.log(LogStatus.INFO, "Verified the error message and refresh button");
						reportPass("demo Test Passed");
						takeScreenShot();
						
					}
					else{
						reportFailure("Error test not present for selecting atleast one video");
					}
			}
		 else if (username.equalsIgnoreCase("")||email.equalsIgnoreCase(""))
		{
			test.log(LogStatus.INFO, "Name or email or both are empty ");
			System.out.println("clicking on generate if username.equalsIgnoreCase||email.equalsIgnoreCase");
			click("generate_xpath");
			System.out.println("clicking on generate if username.equalsIgnoreCase||email.equalsIgnoreCase");
			
			wait(2);
			boolean error=isElementPresent("error_xpath");
			boolean refresh=isElementPresent("refresh_xpath");
			if (error==true && refresh==true){
				reportPass("demo Test Passed");
				takeScreenShot();
			
			}
			else{
				reportFailure("Error test not present for required fields");
				
		}
			}
		  else {
	    	 System.out.println("clicking on generate at last");
		     click("generate_xpath");
		     System.out.println("clicked on generate at last");
			 takeScreenShot();
		  }
		
	}
	

	
}
