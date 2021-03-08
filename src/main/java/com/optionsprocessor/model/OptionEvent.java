package com.optionsprocessor.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
public class OptionEvent {
    private String symbol;
    private String status;
    private Object underlying;
    private String strategy;
    private BigDecimal interval;
    private Boolean isDelayed;
    private Boolean isIndex;
    private BigDecimal interestRate;
    private BigDecimal underlyingPrice;
    private BigDecimal volatility;
    private BigDecimal daysToExpiration;
    private Integer numberOfContracts;
    private List<StrikePrice> putStrikePrices;
    private List<StrikePrice> callStrikePrices;
}