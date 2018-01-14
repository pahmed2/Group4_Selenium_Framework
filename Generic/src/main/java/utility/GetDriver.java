package utility;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class GetDriver {

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
    public static String url = "";

    @Parameters({"useCloudEnv","cloudEnv","os","browserName","browserVersion","url", "testName","os_version","resolution"})
    @BeforeMethod
    public void setUp(@Optional("false") boolean useCloudEnv, String cloudEnv, @Optional("Windows 8") String os, @Optional("firefox") String browserName, @Optional("34")
            String browserVersion, @Optional("http://www.amazon.com") String url, String testName, String os_version, String resolution)throws IOException {
        this.useCloudEnv = useCloudEnv;
        this.cloudEnv = cloudEnv;
        this.os = os;
        this.browserName = browserName;
        this.testName = testName;
        this.os_version = os_version;
        this.resolution = resolution;
        this.url = url;

    }
//    @AfterMethod
//    public void tearDown(){
//        closeDriver();
//    }

    public WebDriver getDriver()throws IOException {
        WebDriver locDriver = null;

        if (useCloudEnv == true) {
            //run in cloud
            locDriver=getCloudDriver(cloudEnv, os, browserName, browserVersion, testName, os_version, resolution);

        } else {
            //run in local
            locDriver=getLocalDriver(os, browserName);

        }

        locDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        locDriver.manage().timeouts().pageLoadTimeout(35, TimeUnit.SECONDS);
        locDriver.get(url);
        //locDriver.manage().window().maximize();

        return locDriver;
    }

    public WebDriver getLocalDriver(@Optional("mac") String OS,String browserName){
        WebDriver locDriver = null;
        if(browserName.equalsIgnoreCase("chrome")){
            if(OS.equalsIgnoreCase("Mac")){
                System.setProperty("webdriver.chrome.driver", "../Generic/driver/chromedriver");
            }else if(OS.equalsIgnoreCase("Win")){
                System.setProperty("webdriver.chrome.driver", "../Generic/driver/chromedriver.exe");
            }
            locDriver = new ChromeDriver();
        }else if(browserName.equalsIgnoreCase("firefox")){
            if(OS.equalsIgnoreCase("Mac")){
                System.setProperty("webdriver.gecko.driver", "../Generic/driver/geckodriver");
            }else if(OS.equalsIgnoreCase("Win")) {
                System.setProperty("webdriver.gecko.driver", "../Generic/driver/geckodriver.exe");
            }
            locDriver = new FirefoxDriver();

        } else if(browserName.equalsIgnoreCase("ie")) {
            System.setProperty("webdriver.ie.driver", "../Generic/driver/IEDriverServer.exe");
            locDriver = new InternetExplorerDriver();
        }
        return locDriver;

    }
    public WebDriver getLocalGridDriver(String browserName) {
        WebDriver locDriver = null;

        if (browserName.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver", "../Generic/driver/chromedriver");
            locDriver = new ChromeDriver();
        } else if (browserName.equalsIgnoreCase("firefox")) {
            System.setProperty("webdriver.gecko.driver", "../Generic/driver/geckodriver");
            locDriver = new FirefoxDriver();
        } else if (browserName.equalsIgnoreCase("ie")) {
            System.setProperty("webdriver.ie.driver", "../Generic/browser-driver/IEDriverServer.exe");
            locDriver = new InternetExplorerDriver();
        }
        return locDriver;
    }


    public WebDriver getCloudDriver(String env,String os, String browserName,
                                    String browserVersion, String testName, String os_version,String resolution)throws IOException {
        WebDriver locDriver = null;
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("platform", os);
        cap.setBrowserName(browserName);
        cap.setCapability("version",browserVersion);
        cap.setCapability("os", os);
        if(env.equalsIgnoreCase("Saucelabs")){
            cap.setCapability("name", testName);
            locDriver = new RemoteWebDriver(new URL("http://"+SAUCE_USERNAME+":"+SAUCE_ACCESS_KEY+
                    "@ondemand.saucelabs.com:80/wd/hub"), cap);
        }else if(env.equalsIgnoreCase("Browserstack")) {
            cap.setCapability("os_version", os_version);
            cap.setCapability("resolution", resolution);
            locDriver = new RemoteWebDriver(new URL("http://" + BROWSERSTACK_USERNAME + ":" + BROWSERSTACK_ACCESS_KEY +
                    "@hub-cloud.browserstack.com/wd/hub"), cap);
        }
        return locDriver;
    }

    public void closeDriver(WebDriver driver)  {
        driver.quit();
        driver = null;
    }
}