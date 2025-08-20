package com.example.cv_analyzer.repository;

import com.example.cv_analyzer.domain.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobOfferRepo extends JpaRepository<JobOffer, Long> {


}
