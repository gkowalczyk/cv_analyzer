package com.example.cv_analyzer.config;

import com.example.cv_analyzer.domain.JobOffer;
import com.example.cv_analyzer.domain.JustJoinItWebStrategy;
import com.example.cv_analyzer.domain.Web;
import com.example.cv_analyzer.infrastructure.scraper.justjoinit.JustJoinItJobOfferExtractorImpl;
import com.example.cv_analyzer.infrastructure.scraper.justjoinit.Validator;
import com.example.cv_analyzer.infrastructure.scraper.justjoinit.WebDriverProvider;
import com.example.cv_analyzer.infrastructure.scraper.justjoinit.WebScraperEngine;
import com.example.cv_analyzer.infrastructure.file.DocumentService;
import org.openqa.selenium.WebElement;
import org.springframework.ai.document.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Configuration
public class Config {

    @Bean
    @Description("Pobierz moje CV")
    public Function<DocumentService.Request, List<Document>> getMyCv(DocumentService documentService) {
        return documentService;
    }

    @Bean
    @Description("Pobierz oferty pracy z https://justjoin.it")
    public Function<JustJoinItWebStrategy.Request, List<JobOffer>> getJobsJustJoinItOffers(JustJoinItJobOfferExtractorImpl<WebElement> extractorImpl) {
        return extractorImpl;
    }

    @Bean
    public JustJoinItJobOfferExtractorImpl<WebElement> justJoinItJobOfferExtractor(
            WebScraperEngine engine,
            Validator validator,
            WebDriverProvider webDriverProvider) {
        return new JustJoinItJobOfferExtractorImpl<>(engine, validator, webDriverProvider);
    }

    @Bean
    @Description("Pobierz oferty pracy ze wszystkich serwisów")
    public Function<Web.Request, List<JobOffer>> getOffersAdapter(
            JustJoinItJobOfferExtractorImpl<WebElement> justJoinItExtractor) {
        return request -> {
            List<JobOffer> all;
            try {
                all = new ArrayList<>(justJoinItExtractor.getJobsJustJoinItOffers(request.level(), request.position(), request.city()));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Scraping interrupted", e);
            }
            return all;
        };
    }
}