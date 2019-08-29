package com.saucelabs.customerdemos;

import static io.restassured.RestAssured.given;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.html5.Location;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.restassured.response.Response;
import io.appium.java_client.android.*;

public class AppiumTestMobileWebRealDeviceAndroid {
	
	
	   protected static Response response;

	   static String requestBodyTestPassed = "{\"passed\" : true }";
	   static String requestBodyTestFailed = "{\"passed\" : false }";

	
	static String URL= "https://us1.appium.testobject.com/wd/hub";

    public static void main(final String[] args) throws InterruptedException, MalformedURLException {

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("testobject_api_key", "CDAA401708C84754A27500099B762547");   //Cisco Web Project
        caps.setCapability("deviceName", "Google_Pixel_real_us");
        caps.setCapability("platformVersion", "9");
        caps.setCapability("platformName", "Android");
        caps.setCapability("appiumVersion", "1.10.1");
        caps.setCapability("automationName", "UiAutomator2");
	    caps.setCapability("name",  "AppiumTest MobileWeb RealDevice Android");

        //caps.setCapability("newCommandTimeout", 90);


        AppiumDriver<MobileElement> driver = new AndroidDriver<>(new java.net.URL(URL), caps); 

        driver.get("https://www.google.com");
        
    	if (driver.getTitle().equals("Google")) {
		   		
       		response = given().contentType("application/json").auth().basic("muralitulugu", "CDAA401708C84754A27500099B762547").body(requestBodyTestPassed)
    				.when().put("https://app.testobject.com/api/rest/v2/appium/session/"+driver.getSessionId().toString()+"/test");
  
        } else {

         	response = given().contentType("application/json").auth().basic("muralitulugu", "CDAA401708C84754A27500099B762547").body(requestBodyTestFailed)
        				.when().put("https://app.testobject.com/api/rest/v2/appium/session/"+driver.getSessionId().toString()+"/test");
        	
        }        
        
         driver.quit();
    }
    

}

