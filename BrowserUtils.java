
package com.mytestframework.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BrowserUtils {
    public static WebDriver getDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Shah Faisal\\OneDrive\\Documents\\AP\\Java Prg\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        return new ChromeDriver();
    }
}
