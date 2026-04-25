package com.example;

import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import java.util.concurrent.TimeUnit;

public class LoginTest {

    @Test
    public void test_login_with_incorrect_credentials() {
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriver driver = new ChromeDriver(options);
        driver.navigate().to("http://103.139.122.250:4000/");

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        driver.findElement(By.id("email")).sendKeys("qasim@malik.com");
        driver.findElement(By.id("password")).sendKeys("abcdcdefg");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        String errorText = driver.findElement(By.cssSelector("div.bg-red-50 span")).getText();
        assert(errorText.contains("Failed to fetch") || errorText.contains("Incorrect"));

        driver.quit();
    }
}
