package com.saucelabs.customerdemos;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.restassured.response.Response;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.openqa.selenium.OutputType;

import static io.restassured.RestAssured.given;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by muralitulugu on 08/15/2019.
 */
public class AppiumTestMobileNativeAppRealDeviceiOS {

	
    protected static Response response;

    String requestBodyTestPassed = "{\"passed\" : true }";
    String requestBodyTestFailed = "{\"passed\" : false }";
    
    private ThreadLocal<IOSDriver> driver = new ThreadLocal<IOSDriver>();
  
  /**
   * DataProvider that explicitly sets the device combinations to be used.
   *
   * @param testMethod
   * @return
   */
  @DataProvider(name = "devices", parallel = true)
  public static Object[][] sauceBrowserDataProvider(Method testMethod) {
      return new Object[][]{
    		  //Verify that your account has access to the devices below
             new Object[]{"iOS", "iPhone 7", "10"},
             new Object[]{"iOS", "iPhone 8", "11"}
      };
  }  
  
  private IOSDriver createDriver(String platformName, String platformVersion, String deviceName, String methodName) throws MalformedURLException {
  	
      DesiredCapabilities capabilities = new DesiredCapabilities();
      capabilities.setCapability("testobject_api_key", "903EDEC92D0B4E81896327A304B4BDFC");		//Calculator3 Project
      capabilities.setCapability("deviceName", deviceName);
      capabilities.setCapability("platformVersion", platformVersion);
      capabilities.setCapability("platformName", platformName);
      capabilities.setCapability("name",  methodName);
      capabilities.setCapability("appiumVersion", "1.13.0");
      
      driver.set(new IOSDriver<WebElement>(
              new URL("https://us1.appium.testobject.com/wd/hub"),
              capabilities));
      return driver.get();
  }

    /* A simple addition, it expects the correct result to appear in the result field. */
    @Test(dataProvider = "devices")
    public void twoPlusThreeOperation(String platformName, String deviceName, String platformVersion, Method method) throws MalformedURLException {

    	IOSDriver driver = createDriver(platformName, platformVersion, deviceName, method.getName());

        /* Get the elements. */
        MobileElement buttonTwo = (MobileElement)(driver.findElement(MobileBy.AccessibilityId("2")));
        MobileElement buttonThree = (MobileElement)(driver.findElement(MobileBy.AccessibilityId("3")));
        MobileElement buttonPlus = (MobileElement)(driver.findElement(MobileBy.AccessibilityId("+")));
        MobileElement buttonEquals = (MobileElement)(driver.findElement(MobileBy.AccessibilityId("=")));
        MobileElement resultField = (MobileElement)(driver.findElement(By.xpath("//XCUIElementTypeStaticText|//UIAApplication[1]/UIAWindow[1]/UIAStaticText[1]")));

        /* Add two and two. */
        buttonTwo.click();
        buttonPlus.click();
        buttonThree.click();
        driver.getScreenshotAs(OutputType.FILE);
        buttonEquals.click();
        driver.getScreenshotAs(OutputType.FILE);
        
        /* Check if within given time the correct result appears in the designated field. */
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.textToBePresentInElement(resultField, "5"));
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
    	
    	IOSDriver driver = getWebDriver();

      	if (result.isSuccess()) {
	   		
       		response = given().contentType("application/json").auth().basic("muralitulugu", "CDAA401708C84754A27500099B762547").body(requestBodyTestPassed)
    				.when().put("https://app.testobject.com/api/rest/v2/appium/session/"+driver.getSessionId().toString()+"/test");
    	

        	} else {

         	response = given().contentType("application/json").auth().basic("muralitulugu", "CDAA401708C84754A27500099B762547").body(requestBodyTestFailed)
        			.when().put("https://app.testobject.com/api/rest/v2/appium/session/"+driver.getSessionId().toString()+"/test");
        	
        	}

        driver.quit();
    }
    
    /**
     * @return the {@link WebDriver} for the current thread
     */
    public IOSDriver getWebDriver() {
        return driver.get();
    }
}
