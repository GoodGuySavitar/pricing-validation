package com.pricing.pricing_proj;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.pricing.pricing_proj.data.PricingRecord;
import com.pricing.pricing_proj.data.PricingRecordRepository;

@SpringBootApplication
public class PricingProjApplication {

	public static void main(String[] args) {
		SpringApplication.run(PricingProjApplication.class, args);
	}

    //THIS IS A FUNCTION THAT RUNS ONCE THE APPLICATION STARTS 
    @Bean
    CommandLineRunner probeDb(PricingRecordRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                PricingRecord r = new PricingRecord();
                r.setInstrumentGuid("TEST-1");
                r.setTradeDate("2024-05-02");
                r.setPrice(new BigDecimal("101.50"));
                r.setExchange("NASDAQ");
                r.setProductType("EQUITY");
                repo.save(r);
            }
            System.out.println("Pricing records in DB: " + repo.count());
        };
    }
}
