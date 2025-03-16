package jonathan.modern_design.account_module.dtos;

import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.user.domain.User.UserId;

import java.math.BigDecimal;

public record AccountDto(String accountNumber, BigDecimal balance, String currency, UserId userId) {
    public AccountDto(final Account account) {
        this(account.getAccountNumber().getValue(), account.getMoney().getAmount(), account.getMoney().getCurrency().getDescription(), account.getUserId());
    }
}
