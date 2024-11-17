package com.example.cv_analyzer.config;


import com.example.cv_analyzer.domain.JobOffer;
import com.example.cv_analyzer.service.DocumentService;
import com.example.cv_analyzer.service.JobsOffersService;
import org.springframework.ai.document.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.List;
import java.util.function.Function;

@Configuration
public class Config {

    @Bean
    @Description("Pobierz moje CV")
    public Function<DocumentService.Request, List<Document>> getMyCv() {
        return new DocumentService();
    }
    @Bean
    @Description("Pobierz oferty pracy z limitem")
    public Function<JobsOffersService.Request, List<JobOffer>> getJobsOffers() {
        return new JobsOffersService();
    }
}
