package jonathan.modern_design._shared.domain.catalogs;

import jonathan.modern_design._config.exception.RootException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;

@Getter
@RequiredArgsConstructor
public enum Currency {
    USD("USD", "US_DOLLAR"),
    EUR("EUR", "EURO"),
    GBP("GBP", "BRITISH_POUND");

    private final String code;
    private final String description;

    public static Currency fromDesc(String desc) {
        for (Currency currency : values()) {
            if (currency.getDescription().equalsIgnoreCase(desc)) {
                return currency;
            }
        }
        throw new CurrencyNotFoundException(desc);
    }

    public static Currency fromCode(String code) {
        for (Currency currency : values()) {
            if (currency.getCode().equalsIgnoreCase(code)) {
                return currency;
            }
        }
        throw new CurrencyNotFoundException(code);
    }

    static class CurrencyNotFoundException extends RootException {
        @Serial private static final long serialVersionUID = 800745090304627590L;

        public CurrencyNotFoundException(String code) {
            super("Currency not found for code " + code);
        }
    }
}
