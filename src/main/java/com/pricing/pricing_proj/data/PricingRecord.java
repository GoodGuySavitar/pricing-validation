package com.pricing.pricing_proj.data;

import java.math.BigDecimal;

import jakarta.persistence.*;

//CREATING THE FORMAT IN WHICH THE DATA WILL BE STORED IN THE DATABASE

@Entity
@Table(name = "pricing_records")
public class PricingRecord {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String instrumentGuid;

    @Column(nullable = false) 
    private String tradeDate;

    @Column(precision = 18, scale = 6) 
    private BigDecimal price;

    @Column(nullable = false) 
    private String exchange;

    @Column(nullable = false) 
    private String productType;

    //GETTERS AND SETTERS
    public Long getId() {
        return id;
    }           

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstrumentGuid() {
        return instrumentGuid;
    }

    public void setInstrumentGuid(String instrumentGuid) {
        this.instrumentGuid = instrumentGuid;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}


