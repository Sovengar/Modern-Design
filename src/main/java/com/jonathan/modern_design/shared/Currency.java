package com.jonathan.modern_design.shared;

public enum Currency {
    US_DOLLAR("USD"),
    EURO("EUR"),
    BRITISH_POUND("GBP");

    private final String code;

    Currency(String code) {
        this.code = code;
    }

    public static Currency fromCode(String code) {
        for (Currency currency : values()) {
            if (currency.getCode().equalsIgnoreCase(code)) {
                return currency;
            }
        }
        throw new CurrencyNotFoundException(code);
    }

    public String getCode() {
        return code;
    }

    private static class CurrencyNotFoundException extends RuntimeException {
        public CurrencyNotFoundException(String code) {
            super("Currency not found for code " + code);
        }
    }
}

