package com.example.cv_analyzer.infrastructure.scraper.justjoinit;

import com.example.cv_analyzer.domain.JobOffer;
import com.example.cv_analyzer.domain.JustJoinItWebStrategy;
import com.example.cv_analyzer.exception.JobOfferScrapingException;
import com.example.cv_analyzer.domain.JobOfferExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class JustJoinItJobOfferExtractorImpl<T extends WebElement>
        implements JobOfferExtractor<T>, Function<JustJoinItWebStrategy.Request, List<JobOffer>> {

    private final WebScraperEngine webScraperEngine;
    private final Validator validator;
    private final WebDriverProvider webDriverProvider;

    @Override
    public JobOffer extract(T offer) {
        String title = webScraperEngine.extractTitle(offer);
        String company = webScraperEngine.extractCompany(offer);
        String city = webScraperEngine.extractCity(offer);
        List<String> skillList = webScraperEngine.extractSkills(offer);
        String salaryRaw = webScraperEngine.extractSalary(offer);
        String link = webScraperEngine.extractLinkToOffer(offer);

        return JobOffer.builder()
                .offerName(title)
                .companyName(company)
                .workplaceType(city)
                .salaryRaw(salaryRaw)
                .link(link)
                .requiredSkills(skillList)
                .build();
    }

    @Override
    public List<JobOffer> apply(JustJoinItWebStrategy.Request request) {
        try {
            return getJobsJustJoinItOffers(request.level(), request.position(), request.city());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new JobOfferScrapingException("Scraping interrupted for: " + request, e);
        } catch (Exception e) {
            throw new JobOfferScrapingException("Unexpected error during scraping: " + request, e);
        }
    }

    public List<JobOffer> getJobsJustJoinItOffers(String level, String position, String location) throws InterruptedException {
        WebDriver webDriver = webDriverProvider.createWebDriver();

        try {
            String url = buildUrl(level, position, location);
            log.info("Url {} ", url);
            webDriver.get(url);

            By offerSelector = By.xpath("//*[@id='up-offers-list']/ul/li");
            List<WebElement> offers = scrollUntilAllOffersLoaded(webDriver, offerSelector);

            return offers.stream()
                    .map(el -> extract((T) el))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } finally {
            webDriver.quit();
        }
    }

    private List<WebElement> scrollUntilAllOffersLoaded(WebDriver driver, By offerSelector) throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        int previousSize = 0;
        int retries = 0;
        log.info("Starting auto-scroll to load job offers...");

        while (retries < 100) {
            js.executeScript("window.scrollBy(0, 1000);");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            int currentSize = driver.findElements(offerSelector).size();
            log.debug("Scroll iteration {}: offers increased from {} to {}", retries + 1, previousSize, currentSize);

            if (currentSize <= previousSize) {
                log.info("All job offers loaded (total: {}).", currentSize);
                break;
            }

            previousSize = currentSize;
            retries++;
        }

        List<WebElement> result = driver.findElements(offerSelector);
        log.info("Total job offers found: {}", result.size());
        return result;
    }


    private String buildUrl(String level, String position, String location) {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("justjoin.it")
                .pathSegment("job-offers", validator.validate(location), validator.resolveCategory(position))
                .queryParam("experience-level", validator.validate(level))
                .build()
                .toUriString();
    }
}
