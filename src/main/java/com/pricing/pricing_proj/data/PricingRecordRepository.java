package com.pricing.pricing_proj.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

//SPRING IS GOING TO AUTO GENERATE A REPOSITORY FOR THE ENTITY PRICING RECORD
//A REPOSITORY IS USED TO INTERACT WITH THE DATABASE WITHOUT WRITING ANY ACTUAL SQL QUERIES. 
//^ THIS IS A PART OF SPRING DATA JPA (MY REFERENCE)
public interface PricingRecordRepository extends JpaRepository<PricingRecord, String> {
    boolean existsByInstrumentGuid(String guid);    //THIS IS MADE SPECIFIICALLY WHEN USER WANTS TO ADD A NEW RECORD WITHOUT UPLOADING A FILE.
    List<PricingRecord> findAllByInstrumentGuidIn(List<String> instrumentGuids);
    Optional<PricingRecord> findByInstrumentGuid(String instrumentGuid);
}
