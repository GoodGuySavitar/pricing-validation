package com.pricing.pricing_proj.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

//SPRING IS GOING TO AUTO GENERATE A REPOSITORY FOR THE ENTITY PRICING RECORD
//A REPOSITORY IS USED TO INTERACT WITH THE DATABASE WITHOUT WRITING ANY ACTUAL SQL QUERIES. 
//^ THIS IS A PART OF SPRING DATA JPA (MY REFERENCE)
public interface PricingRecordRepository extends JpaRepository<PricingRecord, Long> {
    List<PricingRecord> findAllByInstrumentGuidIn(List<String> instrumentGuids);
}
