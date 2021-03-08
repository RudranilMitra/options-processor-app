package com.optionsprocessor.model;

import java.util.LinkedHashMap;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PutExpDateMap {
    private LinkedHashMap<String, ExpirationDate> expirationDateMap;
}
