package jonathan.modern_design.account_module.domain.services;

import jonathan.modern_design._shared.Currency;
import jonathan.modern_design._shared.tags.DomainService;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountMoney;

@DomainService
public class CurrencyChanger {
    //Complex or reused logic

    private AccountMoney a;
    private Currency b;

    public void changeCurrency(AccountMoney money, Currency newCurrency) {

        this.a = money;
        this.b = newCurrency;
    }
}
