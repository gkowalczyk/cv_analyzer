package com.example.cv_analyzer.domain;

import com.example.cv_analyzer.infrastructure.scraper.justjoinit.JustJoinItJobOfferExtractorImpl;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JustJoinItWebStrategy extends Web<WebElement> {

    public JustJoinItWebStrategy(JustJoinItJobOfferExtractorImpl<WebElement> extractor) {
        super(extractor);
    }

    public record Request(String level, String position, String city) {
            }
}