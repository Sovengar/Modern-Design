package jonathan.modern_design.banking.api.dtos;

import jonathan.modern_design.auth.domain.models.User;
import jonathan.modern_design.banking.domain.models.Account;
import jonathan.modern_design.banking.domain.models.AccountEntity;

import java.math.BigDecimal;

public record AccountDto(
        String accountNumber,
        BigDecimal balance,
        String currency,
        Account.Status status,
        User.Id userId) {

    //We can use Account.Status or create here an StatusDto if needed.

    public AccountDto(final Account account) {
        this(
                account.getAccountNumber().getAccountNumber(),
                account.getMoney().getBalance(),
                account.getMoney().getCurrency().getDescription(),
                account.getStatus(),
                account.getUserId()
        );
    }

    public AccountDto(final AccountEntity account) {
        this(
                account.getAccountNumber(),
                account.getBalance(),
                account.getCurrency().getDescription(),
                account.getStatus(),
                account.getUserId()
        );
    }
}
