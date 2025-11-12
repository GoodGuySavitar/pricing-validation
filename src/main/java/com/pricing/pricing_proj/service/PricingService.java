package com.pricing.pricing_proj.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.pricing.pricing_proj.controller.PricingCrud;
import com.pricing.pricing_proj.data.PricingRecord;
import com.pricing.pricing_proj.data.PricingRecordRepository;
import com.pricing.pricing_proj.model.PricingRow;

import org.springframework.transaction.annotation.Transactional;


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

    
    //CRUD PART


    @Transactional(readOnly = true)
    public List<PricingRecord> listAll() {
        // optional: sort by id to make results stable
        return repository.findAll(org.springframework.data.domain.Sort.by("id"));
    }

    @Transactional(readOnly = true)
    public PricingRecord getById(Long id){
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found: id=" + id));
    }

    @Transactional
    public PricingRecord create(PricingCrud dto){
        
        if (repository.existsByInstrumentGuid(dto.instrumentGuid().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "instrumentGuid already exists");
        }

        PricingRecord rec = new PricingRecord();
        rec.setInstrumentGuid(dto.instrumentGuid().trim());
        rec.setTradeDate(dto.tradeDate().trim());
        rec.setPrice(dto.price() == null ? null : BigDecimal.valueOf(dto.price()));
        rec.setExchange(dto.exchange().trim());
        rec.setProductType(dto.productType().trim());

        return repository.save(rec);
    }

    @Transactional
    public void update(Long id, PricingCrud dto){
        PricingRecord rec = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found: id=" + id));

        //IF THE USER IS TRYING TO UPDATE THE instrumentGuid, THEN MAKE SURE IT DOESN'T ALREADY EXIST
        String newGuid = dto.instrumentGuid().trim();
        if(!newGuid.equals(rec.getInstrumentGuid()) && repository.existsByInstrumentGuid(newGuid)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "instrumentGuid already exists");
        }

        rec.setInstrumentGuid(newGuid);
        rec.setTradeDate(dto.tradeDate().trim());
        rec.setPrice(dto.price() == null ? null : BigDecimal.valueOf(dto.price()));
        rec.setExchange(dto.exchange().trim());
        rec.setProductType(dto.productType().trim());
        
        repository.save(rec);
    }

    @Transactional
    public void delete(Long id){
        if(!repository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found: id=");
        }
        repository.deleteById(id);
    }   
}
