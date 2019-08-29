package com.saucelabs.customerdemos;

import java.net.URL;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import io.restassured.response.Response;

import org.openqa.selenium.OutputType;

import static io.restassured.RestAssured.given;



public class AppiumTestMobileNativeAppRealDeviceAndroid  {
	
    private AndroidDriver driver;
    
    protected static Response response;

    String requestBodyTestPassed = "{\"passed\" : true }";
    String requestBodyTestFailed = "{\"passed\" : false }";
    
    

    @BeforeMethod
    public void setUp() throws Exception {

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("testobject_api_key", "460D1D57F4144644AC7D7AA28B276453");	//ContactManager Project
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("testobject_test_name", "Add Contact Test");
        
        driver = new AndroidDriver(new URL("http://us1.appium.testobject.com/wd/hub"), capabilities);

    }

    
    @Test
    public void testMethod() throws Exception {
    	
        WebElement button = driver.findElement(By.className("android.widget.Button"));
        button.click();
        driver.getScreenshotAs(OutputType.FILE);
        List<WebElement> textFieldsList = driver.findElements(By.className("android.widget.EditText"));
        textFieldsList.get(0).sendKeys("Murali Tulugu");
        driver.getScreenshotAs(OutputType.FILE);
        button.click();
        driver.getScreenshotAs(OutputType.FILE);
    }

    @AfterMethod
    public void tearDown(ITestResult result){
    	
      	if (result.isSuccess()) {
		   		
       		response = given().contentType("application/json").auth().basic("muralitulugu", "CDAA401708C84754A27500099B762547").body(requestBodyTestPassed)
    				.when().put("https://app.testobject.com/api/rest/v2/appium/session/"+driver.getSessionId().toString()+"/test");
    	

        	} else {

         	response = given().contentType("application/json").auth().basic("muralitulugu", "CDAA401708C84754A27500099B762547").body(requestBodyTestFailed)
        			.when().put("https://app.testobject.com/api/rest/v2/appium/session/"+driver.getSessionId().toString()+"/test");
        	
        	}
      	
        driver.quit();
    }


}

