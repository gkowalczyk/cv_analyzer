package com.example.cv_analyzer.domain;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Web<T> {

    protected JobOfferExtractor<T> jobOfferExtractor;

    public Web(JobOfferExtractor<T> jobOfferExtractor) {
        this.jobOfferExtractor = jobOfferExtractor;
    }

    public record Request(String level, String position, String city) {

    }
}

