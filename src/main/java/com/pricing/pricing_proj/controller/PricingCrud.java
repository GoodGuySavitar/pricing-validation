package com.pricing.pricing_proj.controller;

import jakarta.validation.constraints.*;

//FORMAT FOR HOW THE DATA LOOKS
public record PricingCrud(
    @NotBlank String instrumentGuid,
    @NotBlank String tradeDate,
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
    @Digits(integer = 12, fraction = 6)
    Double price,
    @NotBlank String exchange,
    @NotBlank String productType
){}
