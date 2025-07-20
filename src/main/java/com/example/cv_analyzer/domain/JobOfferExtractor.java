package com.example.cv_analyzer.domain;


public interface JobOfferExtractor<T> {
    JobOffer extract(T element);
}
