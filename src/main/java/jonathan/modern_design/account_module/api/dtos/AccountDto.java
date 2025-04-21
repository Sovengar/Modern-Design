package jonathan.modern_design.account_module.api.dtos;

import jonathan.modern_design.account_module.domain.models.account.Account;
import jonathan.modern_design.account_module.domain.models.account.AccountEntity;
import jonathan.modern_design.user.domain.models.User;

import java.math.BigDecimal;

public record AccountDto(
        String accountNumber,
        BigDecimal balance,
        String currency,
        String address,
        boolean active,
        User.Id userId) {

    public AccountDto(final Account account) {
        this(
                account.accountAccountNumber().accountNumber(),
                account.money().balance(),
                account.money().currency().description(),
                account.address().toString(),
                account.active(),
                account.userId()
        );
    }

    public AccountDto(final AccountEntity account) {
        this(
                account.accountNumber(),
                account.balance(),
                account.currency().description(),
                account.address(),
                account.active(),
                account.userId()
        );
    }
}
