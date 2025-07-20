package com.example.cv_analyzer.infrastructure.scraper.justjoinit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

@Component
public class ChromeHeadlessWebDriverProvider implements WebDriverProvider {
    @Override
    public WebDriver createWebDriver() {
        ChromeOptions options = new ChromeOptions();
       options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080");
        return new ChromeDriver(options);
    }
}
