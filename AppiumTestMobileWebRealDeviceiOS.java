package com.saucelabs.customerdemos;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.lang.reflect.Method;
import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import io.appium.java_client.ios.IOSDriver;
import io.restassured.response.Response;

public class AppiumTestMobileWebRealDeviceiOS {
	
	    
	static String endpointURL= "https://us1.appium.testobject.com/wd/hub";
	static String URL= "https://www.google.com";

    private ThreadLocal<IOSDriver> driver = new ThreadLocal<IOSDriver>();
    private ThreadLocal<String> sessionId = new ThreadLocal<String>();
    
    public WebDriver driver1;


    protected static Response response;

    String requestBodyTestPassed = "{\"passed\" : true }";
    String requestBodyTestFailed = "{\"passed\" : false }";

    @DataProvider(name = "devices", parallel = true)
    public static Object[][] sauceBrowserDataProvider(Method testMethod) {
        return new Object[][]{
      		  //Verify that your account has access to the devices below
               new Object[]{"iOS", "iPhone 7", "10"},
               new Object[]{"iOS", "iPhone 7", "10"}
        };
    }  
	
    public IOSDriver getWebDriver() {
        return driver.get();
    }
	
    public String getSessionId() {
        return sessionId.get();
    }
    
    
	private IOSDriver createDriver(String platformName, String platformVersion, String deviceName, String methodName) throws MalformedURLException {
	  	
	      DesiredCapabilities caps = new DesiredCapabilities();
	      caps.setCapability("testobject_api_key", "CDAA401708C84754A27500099B762547");	//Cisco Web Project
	      caps.setCapability("deviceName", deviceName);
	      caps.setCapability("platformVersion", platformVersion);
	      caps.setCapability("platformName", platformName);
	      caps.setCapability("name",  methodName);
	      caps.setCapability("appiumVersion", "1.13.0");
	      //caps.setCapability("privateDevicesOnly", "true");

	      
	      driver.set(new IOSDriver<WebElement>(new java.net.URL(endpointURL), caps));
	         

	      return driver.get();
	  }
	
	
	@Test(dataProvider = "devices", enabled = true)
	public void getTitleAppiumTestMobileWebRealDeviceiOS(String platformName, String deviceName, String platformVersion, Method method) throws MalformedURLException{
		
    	IOSDriver driver = createDriver(platformName, platformVersion, deviceName, method.getName());
    	
    	driver.get(URL);


		assertEquals(driver.getTitle(),"Google");
		
	} 
	
	@AfterMethod
    public void tearDown(ITestResult result) throws Exception {
		
				  

   	if (result.isSuccess()) {
   		   		
   		response = given().contentType("application/json").auth().basic("muralitulugu", "CDAA401708C84754A27500099B762547").body(requestBodyTestPassed)
				.when().put("https://app.testobject.com/api/rest/v2/appium/session/"+driver.get().getSessionId().toString()+"/test");
	

    	} else {

     		response = given().contentType("application/json").auth().basic("muralitulugu", "CDAA401708C84754A27500099B762547").body(requestBodyTestFailed)
    				.when().put("https://app.testobject.com/api/rest/v2/appium/session/"+driver.get().getSessionId().toString()+"/test");
    	
    	}
		
    	driver.get().quit();
    	driver.remove();


	}

}
