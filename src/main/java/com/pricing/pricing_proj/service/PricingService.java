package com.pricing.pricing_proj.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pricing.pricing_proj.data.PricingRecord;
import com.pricing.pricing_proj.data.PricingRecordRepository;
import com.pricing.pricing_proj.model.PricingRow;

import jakarta.transaction.Transactional;

@Service
public class PricingService {
    private final PricingRecordRepository repository;

    public PricingService(PricingRecordRepository repository){
        this.repository = repository;
    }

    @Transactional
    public int saveAll(List<PricingRow> rows){
        List<PricingRecord> records = rows.stream().map(row -> {
            PricingRecord rec = new PricingRecord();
            rec.setInstrumentGuid(row.instrumentGuid());
            rec.setTradeDate(row.tradeDate());
            rec.setPrice(row.price() != null ? BigDecimal.valueOf(row.price()) : null);
            rec.setExchange(row.exchange());
            rec.setProductType(row.productType());
            return rec;
        }).toList();

        repository.saveAll(records);
        return records.size();
    }   
}
