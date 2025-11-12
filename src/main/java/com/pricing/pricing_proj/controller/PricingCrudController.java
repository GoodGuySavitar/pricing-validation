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

    @GetMapping("/{id}")
    public PricingRecord get(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PricingRecord create(@Valid @RequestBody PricingCrud body) {
        return service.create(body);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @Valid @RequestBody PricingCrud body) {
        service.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
