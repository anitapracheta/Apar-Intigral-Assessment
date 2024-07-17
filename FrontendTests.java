package com.mytestframework.frontend;

import com.mytestframework.utils.BrowserUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.util.List;


public class FrontendTests {

    private WebDriver driver;
    private ExtentReports extent;
    private ExtentTest test;

    @BeforeMethod
    public void setUp() {
        driver = BrowserUtils.getDriver();
        extent = new ExtentReports();
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter("extent.html");
        extent.attachReporter(htmlReporter);
        test = extent.createTest("Frontend Tests");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
           // driver.quit();
            extent.flush();
        }
    }

    @Test
    public void validateSubscriptionPackages() {
        driver.get("https://subscribe.stctv.com/sa-en");
        driver.manage().window().maximize();

        // Array of countries with their expected prices and currencies
        String[][] countriesDetails = {
            {"sa","Saudi Arabia", "15", "25", "60", "SAR"},
            {"kw","Kuwait", "1.2", "2.5", "4.8", "KWD"},
            {"bh","Bahrain", "1.5", "3", "6", "BHD"}
        };
        
        
       

        for (String[] country : countriesDetails) {
        	String code = country[0];
            String countryName = country[1];
            String litePrice = country[2];
            String classicPrice = country[3];
            String premiumPrice = country[4];
            String currency = country[5];

            validateCountryPackages(code,countryName, litePrice, classicPrice, premiumPrice, currency);
        }
    }

    private void validateCountryPackages(String code,String country, String litePrice, String classicPrice, String premiumPrice, String currency) {
        
    	
        WebElement countryButton = driver.findElement(By.cssSelector("#country-name"));
        countryButton.click();
        driver.findElement(By.id(code)).click();;
        
        

        // Wait for the page to load the packages (you might need to use WebDriverWait for a better wait)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
      
        List<WebElement> packageNames = driver.findElements(By.className("plan-title"));
        for (WebElement packageName:packageNames) {
        	String type = packageName.getText().trim();
        	String type_l=type.toLowerCase();
        	List<WebElement> packageElements = driver.findElements(By.className("plan-name"));
       
        for (WebElement packageElement : packageElements) {
            
            String price = packageElement.findElement(By.cssSelector("div[id='currency-"+type_l+"'] b")).getText().trim();
            
            String Curr_text = packageElement.findElement(By.cssSelector("div[id='currency-"+type_l+"'] i")).getText();
            String packageCurrency= Curr_text.substring(0, Curr_text.indexOf('/'));
            System.out.println("Country: " + country + ", Type: " + type + ", Price: " + price + ", Currency: " + packageCurrency);

            switch (type) {
                case "LITE":
                    Assert.assertEquals(price, litePrice, "Lite package price mismatch in " + country);
                    Assert.assertEquals(packageCurrency, currency, "Lite package currency mismatch in " + country);
                    break;
                case "Classic":
                    Assert.assertEquals(price, classicPrice, "Classic package price mismatch in " + country);
                    Assert.assertEquals(packageCurrency, currency, "Classic package currency mismatch in " + country);
                    break;
                case "Premium":
                    Assert.assertEquals(price, premiumPrice, "Premium package price mismatch in " + country);
                    Assert.assertEquals(packageCurrency, currency, "Premium package currency mismatch in " + country);
                    break;
                default:
                    Assert.fail("Unknown package type: " + type);
            }
        }
    }
    
}
}
        
    










