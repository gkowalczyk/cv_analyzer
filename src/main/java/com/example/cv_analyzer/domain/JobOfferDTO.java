package com.example.cv_analyzer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobOfferDTO {
    private String offerName;
    private CompanyDTO company;
    private String workplaceType;
    private String salaryRaw;
    private String link;
    private List<String> requiredSkills;
}


