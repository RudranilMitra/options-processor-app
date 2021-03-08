package com.optionsprocessor.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Builder
@Getter
@Setter
public class StrikePrice {
//    private String tickerSymbol;
//    private String status;
//    private Object underlying;
//    private String strategy;
//    private Double interval;
//    private Boolean isDelayed;
//    private Boolean isIndex;
//    private Double interestRate;
//    private Double underlyingPrice;

    private String putCall;
    private String symbol;
    private String description;
    private String exchangeName;
    private BigDecimal bid;
    private BigDecimal ask;
    private BigDecimal last;
    private BigDecimal mark;
    private BigInteger bidSize;
    private BigInteger askSize;
    private String bidAskSize;
    private BigInteger lastSize;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal openPrice;
    private BigDecimal closePrice;
    private BigInteger totalVolume;
    private Object tradeDate;
    private BigInteger tradeTimeInLong;
    private BigInteger quoteTimeInLong;
    private BigDecimal netChange;
    private BigDecimal volatility;
    private BigDecimal delta;
    private BigDecimal gamma;
    private BigDecimal theta;
    private BigDecimal vega;
    private BigDecimal rho;
    private BigInteger openInterest;
    private BigDecimal timeValue;
    private BigDecimal theoreticalOptionValue;
    private BigDecimal theoreticalVolatility;
    private Object optionDeliverablesList;
    private BigDecimal strikePrice;
    private BigInteger expirationDate;
    private BigInteger daysToExpiration;
    private String expirationType;
    private BigInteger lastTradingDay;
    private BigDecimal multiplier;
    private String settlementType;
    private String deliverableNote;
    private Boolean isIndexOption;
    private BigDecimal percentChange;
    private BigDecimal markChange;
    private BigDecimal markPercentChange;
    private Boolean nonStandard;
    private Boolean inTheMoney;
    private Boolean mini;
}
