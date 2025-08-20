package com.example.cv_analyzer.domain;


public interface JobOfferExtractor<T> {
    JobOfferDTO extract(T element);
}
