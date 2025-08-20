package com.example.cv_analyzer.repository;

import com.example.cv_analyzer.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepo extends JpaRepository<Company, Long> {

    Optional<Company> findByCompany(String company);
}
