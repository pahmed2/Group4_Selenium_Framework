package base;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utility.DriverFactory;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommonAPI {
    public WebDriver driver = null;
//    final static Logger logger = null;

    public static Logger getLogger(Class className){
        return Logger.getLogger(className);
    }

//    public WebDriver getDriver(){
//        WebDriver driver =  this.driver;
//        return driver;
//    }

//    public void setDriver(WebDriver driver){
//        this.driver=driver;
//    }

    public static final String SAUCE_USERNAME = System.getenv("SAUCE_USERNAME");
    public static final String SAUCE_ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");

    public static final String BROWSERSTACK_USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    public static final String BROWSERSTACK_ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");

    public static boolean useCloudEnv =false;
    public static String cloudEnv = "";
    public static String os = "";
    public static String browserName = "";
    public static String browserVersion = "";
    public static String testName = "";
    public static String os_version = "";
    public static String resolution = "";


    @Parameters({"useCloudEnv","cloudEnv","os","browserName","browserVersion","url", "testName","os_version","resolution"})
    @BeforeMethod
    public void setUp(@Optional("false") boolean useCloudEnv,String cloudEnv, @Optional("macOS High Sierra ") String os, @Optional("firefox") String browserName, @Optional("34")
            String browserVersion, @Optional("http://www.bestbuy.com") String url, String testName, String os_version,String resolution)throws IOException {
        this.useCloudEnv = useCloudEnv;
        this.cloudEnv = cloudEnv;
        this.os = os;
        this.browserName = browserName;
        this.testName = testName;
        this.os_version = os_version;
        this.resolution = resolution;

        driver = DriverFactory.getInstance().getDriver();
//
//        if(useCloudEnv==true){
//            //run in cloud
//            getCloudDriver(cloudEnv,os,browserName,browserVersion,testName,os_version,resolution);
//
//        }else{
//            //run in local
//            driver = DriverFactory.getInstance().getDriver();
////            getLocalDriver(os, browserName);
//
//        }

        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(35, TimeUnit.SECONDS);
        driver.get(url);
        driver.manage().window().maximize();
//        driver.manage().window().fullscreen();

    }

    public WebDriver getLocalDriver(@Optional("mac") String OS,String browserName){
        if(browserName.equalsIgnoreCase("chrome")){
            if(OS.equalsIgnoreCase("Mac")){
                System.setProperty("webdriver.chrome.driver", "../Generic/driver/chromedriver");
            }else if(OS.equalsIgnoreCase("Win")){
                System.setProperty("webdriver.chrome.driver", "../Generic/driver/chromedriver.exe");
            }
            driver = new ChromeDriver();
        }else if(browserName.equalsIgnoreCase("firefox")){
            if(OS.equalsIgnoreCase("Mac")){
                System.setProperty("webdriver.gecko.driver", "../Generic/driver/geckodriver");
            }else if(OS.equalsIgnoreCase("Win")) {
                System.setProperty("webdriver.gecko.driver", "../Generic/driver/geckodriver.exe");
            }
            driver = new FirefoxDriver();

        } else if(browserName.equalsIgnoreCase("ie")) {
            System.setProperty("webdriver.ie.driver", "../Generic/driver/IEDriverServer.exe");
            driver = new InternetExplorerDriver();
        }
        return driver;

    }
    public WebDriver getLocalGridDriver(String browserName) {
        if (browserName.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver", "../Generic/driver/chromedriver");
            driver = new ChromeDriver();
        } else if (browserName.equalsIgnoreCase("firefox")) {
            System.setProperty("webdriver.gecko.driver", "../Generic/driver/geckodriver");
            driver = new FirefoxDriver();
        } else if (browserName.equalsIgnoreCase("ie")) {
            System.setProperty("webdriver.ie.driver", "../Generic/browser-driver/IEDriverServer.exe");
            driver = new InternetExplorerDriver();
        }
        return driver;
    }

    public WebDriver getCloudDriver(String env,String os, String browserName,
                                    String browserVersion, String testName, String os_version,String resolution)throws IOException {

        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("platform", os);
        cap.setBrowserName(browserName);
        cap.setCapability("version",browserVersion);
        cap.setCapability("os", os);
        if(env.equalsIgnoreCase("Saucelabs")){
            cap.setCapability("name", testName);
            driver = new RemoteWebDriver(new URL("http://"+SAUCE_USERNAME+":"+SAUCE_ACCESS_KEY+
                    "@ondemand.saucelabs.com:80/wd/hub"), cap);
        }else if(env.equalsIgnoreCase("Browserstack")) {
            cap.setCapability("os_version", os_version);
            cap.setCapability("resolution", resolution);
            driver = new RemoteWebDriver(new URL("http://" + BROWSERSTACK_USERNAME + ":" + BROWSERSTACK_ACCESS_KEY +
                    "@hub-cloud.browserstack.com/wd/hub"), cap);
        }
        return driver;
    }


    @AfterMethod
    public void tearDown() throws Exception {
        DriverFactory.getInstance().removeDriver();
//        driver.quit();
//        driver = null;
    }

    public void clickByCss(String locator) {
        driver.findElement(By.cssSelector(locator)).click();
    }

    public void clickByXpath(String locator) {
        try {
            driver.findElement(By.xpath(locator)).click();
        }catch (Exception ex){
            driver.findElement(By.xpath(locator)).click();
        }
    }

    public void clickByElement(WebElement locator) {
        try {
            locator.click();
        }catch (Exception ex){
            locator.click();
        }
    }


    public void typeByCss(String locator, String value) {
        driver.findElement(By.cssSelector(locator)).sendKeys(value);
    }
    public void typeByCssNEnter(String locator, String value) {
        driver.findElement(By.cssSelector(locator)).sendKeys(value, Keys.ENTER);
    }

    public void typeByXpath(String locator, String value) {
        try {
            driver.findElement(By.xpath(locator)).sendKeys(value);
        } catch (Exception ex) {
            driver.findElement(By.xpath(locator)).click();
        }
    }

    public void typeByElement(WebElement locator, String value) {
        try {
            locator.sendKeys(value);
        } catch (Exception ex) {
            locator.sendKeys(value);
        }
    }

    public void takeEnterKeys(String locator) {
        driver.findElement(By.cssSelector(locator)).sendKeys(Keys.ENTER);
    }

    public void clearInputField(String locator){
        driver.findElement(By.cssSelector(locator)).clear();
    }
    public List<WebElement> getListOfWebElementsById(String locator) {
        List<WebElement> list = new ArrayList<WebElement>();
        list = driver.findElements(By.id(locator));
        return list;
    }
    public List<String> getTextFromWebElements(String locator){
        List<WebElement> element = new ArrayList<WebElement>();
        List<String> text = new ArrayList<String>();
        element = driver.findElements(By.cssSelector(locator));
        for(WebElement web:element){
            text.add(web.getText());
        }

        return text;
    }
    public List<WebElement> getListOfWebElementsByCss(String locator) {
        List<WebElement> list = new ArrayList<WebElement>();
        list = driver.findElements(By.cssSelector(locator));
        return list;
    }
    public List<WebElement> getListOfWebElementsByXpath(String locator) {
        List<WebElement> list = new ArrayList<WebElement>();
        list = driver.findElements(By.xpath(locator));
        return list;
    }
    public String  getCurrentPageUrl(){
        String url = driver.getCurrentUrl();
        return url;
    }
    public void navigateBack(){
        driver.navigate().back();
    }
    public void navigateForward(){
        driver.navigate().forward();
    }
    public String getTextByCss(String locator){
        String st = driver.findElement(By.cssSelector(locator)).getText();
        return st;
    }
    public String getTextByXpath(String locator){
        String st = driver.findElement(By.xpath(locator)).getText();
        return st;
    }
    public String getTextByWebElement(WebElement locator){
        try {
            String st = locator.getText();
            return st;
        }catch (Exception ex){
            return "";
        }
    }

    public String getTextById(String locator){
        return driver.findElement(By.id(locator)).getText();
    }
    public String getTextByName(String locator){
        String st = driver.findElement(By.name(locator)).getText();
        return st;
    }

    public List<String> getListOfString(List<WebElement> list) {
        List<String> items = new ArrayList<String>();
        for (WebElement element : list) {
            items.add(element.getText());
        }
        return items;
    }
    public void selectOptionByVisibleText(WebElement element, String value) {
        Select select = new Select(element);
        select.selectByVisibleText(value);
    }
    public void sleepFor(int sec)throws InterruptedException{
        Thread.sleep(sec * 1000);
    }
    public void mouseHoverByCSS(String locator){
        try {
            WebElement element = driver.findElement(By.cssSelector(locator));
            Actions action = new Actions(driver);
            Actions hover = action.moveToElement(element);
        }catch(Exception ex){
            System.out.println("First attempt has been done, This is second try");
            WebElement element = driver.findElement(By.cssSelector(locator));
            Actions action = new Actions(driver);
            action.moveToElement(element).perform();

        }

    }
    public void mouseHoverByXpath(String locator){
        try {
            WebElement element = driver.findElement(By.xpath(locator));
            Actions action = new Actions(driver);
            Actions hover = action.moveToElement(element);
        }catch(Exception ex){
            System.out.println("First attempt has been done, This is second try");
            WebElement element = driver.findElement(By.cssSelector(locator));
            Actions action = new Actions(driver);
            action.moveToElement(element).perform();

        }

    }
    //handling Alert
    public void okAlert(){
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }
    public void cancelAlert(){
        Alert alert = driver.switchTo().alert();
        alert.dismiss();
    }

    //iFrame Handle
    public void iframeHandle(WebElement element){
        driver.switchTo().frame(element);
    }

    public void goBackToHomeWindow(){
        driver.switchTo().defaultContent();
    }

    //get Links
    public void getLinks(String locator){
        driver.findElement(By.linkText(locator)).findElement(By.tagName("a")).getText();
    }

    //Taking Screen shots
    public void takeScreenShot()throws IOException {
        File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(file,new File("screenShots.png"));
    }

    //Synchronization
    public void waitUntilClickAble(By locator){
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    public void waitUntilVisible(WebElement webElement){
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement element = wait.until(ExpectedConditions.visibilityOf(webElement));
    }
    public void waitUntilSelectable(By locator){
        WebDriverWait wait = new WebDriverWait(driver, 10);
        boolean element = wait.until(ExpectedConditions.elementToBeSelected(locator));
    }
    public void upLoadFile(String locator,String path){
        driver.findElement(By.cssSelector(locator)).sendKeys(path);
        /* path example to upload a file/image
           path= "C:\\Users\\rrt\\Pictures\\ds1.png";
         */
    }
    public void clearInput(String locator){
        driver.findElement(By.cssSelector(locator)).clear();
    }
    public void keysInput(String locator){
        driver.findElement(By.cssSelector(locator)).sendKeys(Keys.ENTER);
    }

    public String getTitle(){return driver.getTitle();}

    public void takeScreenShot(String fileName )throws IOException {
        File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(file,new File(fileName));
    }
    //S
    public void selectDropDownValueByXPATH(String visibleText, String path){
        Select dropdown = new Select(driver.findElement(By.xpath(path)));
        dropdown.selectByValue(visibleText);
    }

    public boolean isElementPresentByXPATH(String path){
        try {
            if (driver.findElement(By.xpath(path)).isDisplayed()) {
                return true;
            } else return false;
        }catch (Exception ex){
            return false;
        }
    }

    public boolean isElementPresent(WebElement webElement) {
        try {
            if (webElement.isDisplayed()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }
    public void clickIfElementPresent(WebElement webElement){
        if(isElementPresent(webElement)==true) {
            webElement.click();
        }
    }
}