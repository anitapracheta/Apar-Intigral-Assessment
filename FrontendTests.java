package com.mytestframework.frontend;

import com.mytestframework.utils.BrowserUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.time.Duration;
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
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        if (extent != null) {
            extent.flush();
        }
    }

    @DataProvider(name = "countriesData")
    public Object[][] countriesData() {
        return new Object[][]{
            {"sa", "Saudi Arabia", "15", "25", "60", "SAR"},
            {"kw", "Kuwait", "1.2", "2.5", "4.8", "KWD"},
            {"bh", "Bahrain", "2", "3", "6", "BHD"}
        };
    }

    @Test(dataProvider = "countriesData")
    public void validateSubscriptionPackages(String code, String country, String litePrice, String classicPrice, String premiumPrice, String currency) {
        test = extent.createTest("validateSubscriptionPackages - " + country);
        test.info("Starting test for country: " + country);

        driver.get("https://subscribe.stctv.com/sa-en");
        driver.manage().window().maximize();
        test.info("Navigated to subscription page");

        validateCountryPackages(code, country, litePrice, classicPrice, premiumPrice, currency);
    }

    private void validateCountryPackages(String code, String country, String litePrice, String classicPrice, String premiumPrice, String currency) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.findElement(By.cssSelector("#country-name")).click();
        driver.findElement(By.id(code)).click();
        test.info("Selected country: " + country);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("plan-title")));

        List<WebElement> packageNames = driver.findElements(By.className("plan-title"));
        for (WebElement packageName : packageNames) {
            String type = packageName.getText().trim().toLowerCase();

            WebElement packageElement = driver.findElement(By.cssSelector("div[id='currency-" + type + "']"));
            String price = packageElement.findElement(By.tagName("b")).getText().trim();
            String packageCurrency = packageElement.findElement(By.tagName("i")).getText().split("/")[0].trim();

            test.info("Country: " + country + ", Type: " + type + ", Price: " + price + ", Currency: " + packageCurrency);

            switch (type) {
                case "lite":
                    try {
                        Assert.assertEquals(price, litePrice, "Lite package price mismatch in " + country);
                        Assert.assertEquals(packageCurrency, currency, "Lite package currency mismatch in " + country);
                        test.pass("Lite package validation passed for " + country);
                    } catch (AssertionError e) {
                        test.fail("Lite package validation failed for " + country + ": " + e.getMessage());
                        throw e;
                    }
                    break;
                case "classic":
                    try {
                        Assert.assertEquals(price, classicPrice, "Classic package price mismatch in " + country);
                        Assert.assertEquals(packageCurrency, currency, "Classic package currency mismatch in " + country);
                        test.pass("Classic package validation passed for " + country);
                    } catch (AssertionError e) {
                        test.fail("Classic package validation failed for " + country + ": " + e.getMessage());
                        throw e;
                    }
                    break;
                case "premium":
                    try {
                        Assert.assertEquals(price, premiumPrice, "Premium package price mismatch in " + country);
                        Assert.assertEquals(packageCurrency, currency, "Premium package currency mismatch in " + country);
                        test.pass("Premium package validation passed for " + country);
                    } catch (AssertionError e) {
                        test.fail("Premium package validation failed for " + country + ": " + e.getMessage());
                        throw e;
                    }
                    break;
                default:
                    test.fail("Unknown package type: " + type);
                    Assert.fail("Unknown package type: " + type);
            }
        }
    }
}