package com.saucelabs.customerdemos;

import java.net.MalformedURLException;

import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class AppiumTestMobileWebiOSSimulator {
	
	  public static final String USERNAME = System.getenv("SAUCE_USERNAME");
	  public static final String ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");
	  
	  public static final String URL = "https://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:443/wd/hub";

	  public static void main(final String[] args) throws InterruptedException, MalformedURLException {

    	 DesiredCapabilities caps = new DesiredCapabilities();
    	 
    	 caps.setCapability("platformName", "iOS");
    	 caps.setCapability("platformVersion", "12.0");
    	 caps.setCapability("deviceName", "iPhone X Simulator");
    	 caps.setCapability("browserName", "safari");
    	 caps.setCapability("name", "AppiumScript - iOS Simulator - Mobile Web");

         AppiumDriver<MobileElement> driver = new IOSDriver<>(new java.net.URL(URL), caps); 

         driver.get("https://www.google.com");
         
    
        ((JavascriptExecutor) driver).executeScript("sauce:job-result=" + (driver.getTitle().equalsIgnoreCase("Google") ? "passed" : "failed"));

        driver.quit();
    }
}

