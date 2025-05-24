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
        Account.Status status,
        User.Id userId) {

    //We can use Account.Status or create here an StatusDto if needed.

    public AccountDto(final Account account) {
        this(
                account.getAccountNumber().getAccountNumber(),
                account.getMoney().getBalance(),
                account.getMoney().getCurrency().getDescription(),
                account.getAddress().toString(),
                account.getStatus(),
                account.getUserId()
        );
    }

    public AccountDto(final AccountEntity account) {
        this(
                account.getAccountNumber(),
                account.getBalance(),
                account.getCurrency().getDescription(),
                account.getAddress(),
                account.getStatus(),
                account.getUserId()
        );
    }
}
