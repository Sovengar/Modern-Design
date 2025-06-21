package jonathan.modern_design.banking.application;

import jonathan.modern_design._shared.Currency;
import jonathan.modern_design._shared.tags.ApplicationService;
import jonathan.modern_design.banking.domain.models.account.vo.AccountMoney;
import jonathan.modern_design.banking.domain.services.CurrencyChanger;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
class ChangeCurrency {
    private final CurrencyChanger currencyChanger;

    public void handle(AccountMoney money, Currency newCurrency) {
        currencyChanger.changeCurrency(money, newCurrency);
    }

    // TODO
    // Change currency taking in consideration updated exchange rates
    // Change amount of money based on the calculation with the new exchange rates


}

