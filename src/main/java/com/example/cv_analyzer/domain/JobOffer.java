package com.example.cv_analyzer.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@NoArgsConstructor
public class JobOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String offerName;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "company_id")
    private Company company;
    private String workplaceType;
    private String salaryRaw;
    private String link;
    @ElementCollection
    private List<String> requiredSkills;
    }

