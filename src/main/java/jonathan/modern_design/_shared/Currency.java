package jonathan.modern_design._shared;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;

@Getter
@RequiredArgsConstructor
public enum Currency {
    USD("US_DOLLAR"),
    EUR("EURO"),
    GBP("BRITISH_POUND");

    private final String description;

    public static Currency fromDesc(String desc) {
        for (Currency currency : values()) {
            if (currency.getDescription().equalsIgnoreCase(desc)) {
                return currency;
            }
        }
        throw new CurrencyNotFoundException(desc);
    }

    static class CurrencyNotFoundException extends RuntimeException {
        @Serial private static final long serialVersionUID = 800745090304627590L;

        public CurrencyNotFoundException(String code) {
            super("Currency not found for code " + code);
        }
    }
}
