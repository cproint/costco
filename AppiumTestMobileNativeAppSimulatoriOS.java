package com.saucelabs.customerdemos;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.UnexpectedException;

/**
 * Created by muralitulugu on 08/15/2019.
 */
public class AppiumTestMobileNativeAppSimulatoriOS {

    private ThreadLocal<IOSDriver> iosDriver = new ThreadLocal<IOSDriver>();
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
  @DataProvider(name = "simulators", parallel = true)
  public static Object[][] sauceSimulatorDataProvider(Method testMethod) {
      return new Object[][]{
              new Object[]{"iOS", "iPhone XS Simulator", "12.0", "1.9.1", "portrait"},
              new Object[]{"iOS", "iPhone 6 Simulator", "12.2", "1.13.0", "portrait"}
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
        capabilities.setCapability("uploadVideoOnPass", "false");

        // curl -u $SAUCE_USERNAME:$SAUCE_ACCESS_KEY -X POST -H "Content-Type: application/octet-stream" https://saucelabs.com/rest/v1/storage/$SAUCE_USERNAME/SauceGuineaPig-sim-debug.app.zip?overwrite=true --data-binary @/Users/muralitulugu/MobileApps/SauceGuineaPig-sim-debug.app.zip
        //capabilities.setCapability("app", "sauce-storage:SauceGuineaPig-sim-debug.app.zip");

        String app = "https://github.com/saucelabs-sample-test-frameworks/Java-Junit-Appium-iOS/blob/master/resources/SauceGuineaPig-sim-debug.app.zip?raw=true";
        capabilities.setCapability("app", app);

        // Launch Simulator and load the app. And set it as the current thread
        iosDriver.set(new IOSDriver(
                new URL("https://" + userName + ":" + accessKey + "@ondemand.saucelabs.com:443/wd/hub"),
                capabilities));

        String id = ((RemoteWebDriver) getIOSDriver()).getSessionId().toString();
        sessionId.set(id);
    }

    /* A simple addition, it expects the correct result to appear in the result field. */
    @Test(dataProvider = "simulators")
    public void verifyLinkTest(String platformName,
                               String deviceName,
                               String platformVersion,
                               String appiumVersion,
                               String deviceOrientation,
                               Method method)
            throws MalformedURLException, InvalidElementStateException, UnexpectedException {

        //create webdriver session
        createDriver(platformName, deviceName, platformVersion, appiumVersion, deviceOrientation, method.getName());
        WebDriver driver = getIOSDriver();

        WebElement theActiveLink = driver.findElement(By.id("i am a link"));

        Assert.assertTrue(theActiveLink.isEnabled());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {

        IOSDriver driver = getIOSDriver();

        ((JavascriptExecutor)driver).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));

        driver.quit();
    }
    
    /**
     * @return the {@link WebDriver} for the current thread
     */
    public IOSDriver getIOSDriver() {
        return iosDriver.get();
    }
}
