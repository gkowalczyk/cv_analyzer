package com.example.cv_analyzer.domain;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class JobOffer {
    private String companyName;
    private String offerName;
    private double salaryFromPln;
    private double salaryToPln;
    public String salaryRaw;
    private List<String> requiredSkills;
    private List<String> niceToHaveSkills;
    private String workplaceType;
    private String experienceLevel;
    private String location;
    private String link;
}




