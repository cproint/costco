package com.saucelabs.customerdemos;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.restassured.response.Response;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.Assert;


import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.UnexpectedException;

import static io.restassured.RestAssured.given;

/**
 * Created by muralitulugu on 08/15/2019.
 */
public class AppiumTestMobileNativeAppEmulatorAndroid {

    @FindBy(id = "i_am_a_link")
    private WebElement theActiveLink;

    @FindBy(id = "your_comments")
    private WebElement yourComments;

    private ThreadLocal<AndroidDriver> androidDriver = new ThreadLocal<AndroidDriver>();
    private ThreadLocal<String> sessionId = new ThreadLocal<String>();

    private String userName = System.getenv("SAUCE_USERNAME");
    private String accessKey = System.getenv("SAUCE_ACCESS_KEY");

    public String getSessionId() {
        return sessionId.get();
    }


  
  /**
   * DataProvider that explicitly sets the device combinations to be used.
   *
   * @param testMethod
   * @return
   */
  @DataProvider(name = "emulators", parallel = true)
  public static Object[][] sauceEmulatorDataProvider(Method testMethod) {
      return new Object[][]{
              new Object[]{"Android", "Android Emulator", "8.0", "1.9.1", "portrait"},
              new Object[]{"Android", "Samsung Galaxy S8 GoogleAPI Emulator", "8.0", "1.8.1", "portrait"}
      };
  }

    protected void createDriver(
            String platformName,
            String deviceName,
            String platformVersion,
            String appiumVersion,
            String deviceOrientation,
            String methodName)
            throws MalformedURLException, UnexpectedException {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", platformName);
        capabilities.setCapability("platformVersion", platformVersion);
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("deviceOrientation", deviceOrientation);
        capabilities.setCapability("appiumVersion", appiumVersion);
        capabilities.setCapability("name", methodName);

        // curl -u $SAUCE_USERNAME:$SAUCE_ACCESS_KEY -X POST -H "Content-Type: application/octet-stream" https://saucelabs.com/rest/v1/storage/$SAUCE_USERNAME/SauceGuineaPig-sim-debug.app.zip?overwrite=true --data-binary @/Users/muralitulugu/MobileApps/SauceGuineaPig-sim-debug.app.zip
        //capabilities.setCapability("app", "sauce-storage:SauceGuineaPig-sim-debug.apk");

        String app = "https://github.com/saucelabs-sample-test-frameworks/GuineaPig-Sample-App/blob/master/android/GuineaPigApp-debug.apk?raw=true";

        capabilities.setCapability("app", app);

        // Launch remote browser and set it as the current thread
        androidDriver.set(new AndroidDriver(
                new URL("https://" + userName + ":" + accessKey + "@ondemand.saucelabs.com:443/wd/hub"),
                capabilities));

        String id = ((RemoteWebDriver) getAndroidDriver()).getSessionId().toString();
        sessionId.set(id);
    }

    /* A simple addition, it expects the correct result to appear in the result field. */
    @Test(dataProvider = "emulators")
    public void verifyLinkTest(String platformName,
                               String deviceName,
                               String platformVersion,
                               String appiumVersion,
                               String deviceOrientation,
                               Method method)
            throws MalformedURLException, InvalidElementStateException, UnexpectedException {

        //create webdriver session
        createDriver(platformName, deviceName, platformVersion, appiumVersion, deviceOrientation, method.getName());
        WebDriver driver = getAndroidDriver();

        WebElement theActiveLink = driver.findElement(By.id("i_am_a_link"));

        Assert.assertTrue(theActiveLink.isEnabled());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {

        AndroidDriver driver = getAndroidDriver();

        ((JavascriptExecutor)driver).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));

        driver.quit();
    }
    
    /**
     * @return the {@link WebDriver} for the current thread
     */
    public AndroidDriver getAndroidDriver() {
        return androidDriver.get();
    }
}
