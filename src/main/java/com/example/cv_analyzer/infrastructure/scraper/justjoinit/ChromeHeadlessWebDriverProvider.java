package com.example.cv_analyzer.infrastructure.scraper.justjoinit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;
import java.util.UUID;


@Component
public class ChromeHeadlessWebDriverProvider implements WebDriverProvider {
    @Override
    public WebDriver createWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--user-data-dir=/tmp/chrome-profile-" + UUID.randomUUID());
        return new ChromeDriver(options);
    }
}
