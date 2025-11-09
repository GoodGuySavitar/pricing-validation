package com.pricing.pricing_proj.model;

//STRICT. DOESN'T ALLOW EXTRA OR MISSING FIELDS
public record PricingRow (
        String instrumentGuid,
        String tradeDate,
        Double price,
        String exchange,
        String productType
) {}

