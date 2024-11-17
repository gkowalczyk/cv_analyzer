package com.example.cv_analyzer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class JobOffer {
    private String companyName;
    private double salaryFromPln;
    private double salaryToPln;
    private List<String> requiredSkills;
    private List<String>  niceToHaveSkills;
    private String workplaceType;
    private String experienceLevel;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Location {
        private String city;
        private String street;
        private double latitude;
        private double longitude;
    }
}




