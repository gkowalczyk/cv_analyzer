package com.example.cv_analyzer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String company;

    public Company(String companyName) {
    }
}
