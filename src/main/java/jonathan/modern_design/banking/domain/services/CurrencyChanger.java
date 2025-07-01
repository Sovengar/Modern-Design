package jonathan.modern_design.banking.domain.services;

import jonathan.modern_design._shared.Currency;
import jonathan.modern_design._shared.tags.DomainService;
import jonathan.modern_design._shared.vo.Money;

@DomainService
public class CurrencyChanger {
    //Complex or reused logic

    private Money a;
    private Currency b;

    public void changeCurrency(Money money, Currency newCurrency) {

        this.a = money;
        this.b = newCurrency;
    }
}
