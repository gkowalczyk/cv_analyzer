package com.example.cv_analyzer.service;

import com.example.cv_analyzer.domain.JobOffer;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

@Service
public class JobsOffersService implements Function<JobsOffersService.Request, List<JobOffer>> {

    public record Request(long limit) {
    }

    @SneakyThrows
    @Override
    public List<JobOffer> apply(JobsOffersService.Request request) {
        return getJobsOffers(request.limit);
    }

    public List<JobOffer> getJobsOffers(long limit) throws IOException, InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        List<JobOffer> offerList = new ArrayList<>();

        WebDriver webDriver = new ChromeDriver(options);

        try {
            webDriver.get("https://justjoin.it/job-offers/wroclaw/java?experience-level=junior");
            Thread.sleep(5000);
            List<WebElement> offers = webDriver.findElements(By.cssSelector("a.offer-card"));

            for (int i = 0; i < Math.min(limit, offers.size()); i++) {
                WebElement offerElement = offers.get(i);
                String title = offerElement.getText();
                String link = offerElement.getAttribute("href");

                JobOffer jobOffer = new JobOffer();
                jobOffer.setCompanyName(title);
                jobOffer.setWorkplaceType(link);
                offerList.add(jobOffer);

                System.out.println("Tytuł: " + title);
                System.out.println("Link: " + link);
                System.out.println("-----");
            }

        } finally {
            webDriver.quit();
        }
        return offerList;
    }
}



