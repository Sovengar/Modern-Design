package jonathan.modern_design._shared;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;

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
        @Serial private static final long serialVersionUID = 800745090304627590L;

        public CurrencyNotFoundException(String code) {
            super("Currency not found for code " + code);
        }
    }
}
