package com.optionsprocessor.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

@Builder
@Getter
@Setter
public class ExpirationDate {
    private LinkedHashMap<String,StrikePrice> strikePriceKey;
}