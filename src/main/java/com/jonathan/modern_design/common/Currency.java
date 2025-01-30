package com.jonathan.modern_design.common;

import lombok.val;

public enum Currency {
    US_DOLLAR("USD"),
    EURO("EUR"),
    BRITISH_POUND("GBP");

    private final String code;

    Currency(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

