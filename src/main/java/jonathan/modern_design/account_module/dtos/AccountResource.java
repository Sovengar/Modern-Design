package jonathan.modern_design.account_module.dtos;

import jonathan.modern_design.account_module.domain.model.Account;
import jonathan.modern_design.user_module.domain.User.UserId;

import java.math.BigDecimal;

public record AccountResource(String accountNumber, BigDecimal amount, String currency, UserId userId) {
    public AccountResource(final Account account) {
        this(account.getAccountNumber().getValue(), account.getMoney().getAmount(), account.getMoney().getCurrency().getCode(), account.getUserId());
    }
}
