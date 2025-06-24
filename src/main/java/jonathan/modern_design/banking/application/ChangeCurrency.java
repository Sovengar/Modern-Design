package jonathan.modern_design.banking.application;

import jonathan.modern_design._shared.domain.Currency;
import jonathan.modern_design._shared.domain.vo.Money;
import jonathan.modern_design._shared.tags.ApplicationService;
import jonathan.modern_design.banking.domain.services.CurrencyChanger;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
class ChangeCurrency {
    private final CurrencyChanger currencyChanger;

    public void handle(Money money, Currency newCurrency) {
        currencyChanger.changeCurrency(money, newCurrency);
    }

    // Change currency taking in consideration updated exchange rates
    // Change the amount of money based on the calculation with the new exchange rates
}

