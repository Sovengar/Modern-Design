package com.jonathan.modern_design._shared;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Currency {
    US_DOLLAR("USD"),
    EURO("EUR"),
    BRITISH_POUND("GBP");

    private final String code;

    public static Currency fromCode(String code) {
        for (Currency currency : values()) {
            if (currency.getCode().equalsIgnoreCase(code)) {
                return currency;
            }
        }
        throw new CurrencyNotFoundException(code);
    }

    static class CurrencyNotFoundException extends RuntimeException {
        public CurrencyNotFoundException(String code) {
            super("Currency not found for code " + code);
        }
    }
}
