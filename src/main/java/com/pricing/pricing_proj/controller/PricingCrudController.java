package com.pricing.pricing_proj.controller;

import com.pricing.pricing_proj.data.PricingRecord;
import com.pricing.pricing_proj.service.PricingService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


//THIS IS A CONTROLLER TO HANDLE THE CRUD OPERATIONS FOR PRICING RECORDS
@RestController
@RequestMapping("/api/pricing")
public class PricingCrudController {
    private final PricingService service;

    public PricingCrudController(PricingService service){
        this.service = service;
    }

    @GetMapping
    public List<PricingRecord> listAll() {
        return service.listAll();
    }

    //Fetch by instrumentGuid instead of numeric id
    @GetMapping("/{instrumentGuid}")
    public PricingRecord get(@PathVariable String instrumentGuid) {
        return service.getByInstrumentGuid(instrumentGuid);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PricingRecord create(@Valid @RequestBody PricingCrud body) {
        return service.create(body);
    }

    //Update by current instrumentGuid; body may change instrumentGuid
    @PutMapping("/{instrumentGuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String instrumentGuid, @Valid @RequestBody PricingCrud body) {
        service.updateByInstrumentGuid(instrumentGuid, body);
    }

    @DeleteMapping("/{instrumentGuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String instrumentGuid) {
        service.deleteByInstrumentGuid(instrumentGuid);
    }
}
