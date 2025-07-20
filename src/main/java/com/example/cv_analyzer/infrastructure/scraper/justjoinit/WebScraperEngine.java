package com.example.cv_analyzer.infrastructure.scraper.justjoinit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WebScraperEngine {


    public String extractTitle(WebElement offer) {
        return offer.findElements(By.cssSelector("h3[class*=mui-]"))
                .stream()
                .map(WebElement::getText)
                .filter(text -> text.matches("[A-ZŁŚŻŹĆ].*"))
                .findFirst()
                .orElse("Lack of data");
    }

    public String extractCompany(WebElement offer) {
        return offer.findElements(By.cssSelector("[data-testid='ApartmentRoundedIcon'] + span"))
                .stream()
                .map(WebElement::getText)
                .findFirst()
                .orElse("other");
    }

    public String extractCity(WebElement offer) {
        return offer.findElements(By.cssSelector("[data-testid='PlaceOutlinedIcon'] ~ div span"))
                .stream()
                .map(WebElement::getText)
                .map(String::trim)
                .filter(text -> !text.isEmpty() && Character.isUpperCase(text.charAt(0)))
                .findFirst()
                .orElse("Lack of data");
    }

    public String extractSalary(WebElement offer) {
        return offer.findElements(By.cssSelector("div.MuiBox-root"))
                .stream()
                .map(WebElement::getText)
                .map(String::trim)
                .filter(text ->
                        text.matches("(?i).*\\d+.*(PLN|EUR|USD|CHF|GBP).*") ||
                                text.equalsIgnoreCase("Undisclosed Salary"))
                .findFirst()
                .orElse("Lack of data");
    }

    public List<String> extractSkills(WebElement offer) {
        return offer.findElements(By.cssSelector("div[class^='skill-tag-'] > div > div"))
                .stream()
                .map(WebElement::getText)
                .map(String::trim)
                .filter(text -> !text.isBlank())
                .distinct()
                .collect(Collectors.toList());
    }

    public String extractLinkToOffer(WebElement offer) {
        return offer.findElements(By.cssSelector("a.offer-card"))
                .stream()
                .map(el -> el.getAttribute("href"))
                .filter(href -> href != null && !href.isBlank())
                .map(href -> href.startsWith("http") ? href : "https://justjoin.it" + href)
                .findFirst()
                .orElse("Lack of link");
    }
}
