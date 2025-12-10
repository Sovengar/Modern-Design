package jonathan.modern_design.banking.api.dtos;

import jonathan.modern_design.banking.domain.models.Account;
import jonathan.modern_design.banking.domain.models.AccountEntity;

import java.math.BigDecimal;

public record AccountDto(
        String accountNumber,
        BigDecimal balance,
        String currency,
        Account.AccountStatus status) {

    //We can use Account.Status or create here an StatusDto if needed.

    public AccountDto(final Account account) {
        this(
                account.getAccountNumber(),
                account.getMoney().getBalance(),
                account.getMoney().getCurrency().getDescription(),
                account.getStatus()
        );
    }

    public AccountDto(final AccountEntity account) {
        this(
                account.getAccountNumber(),
                account.getBalance(),
                account.getCurrency().getDescription(),
                account.getStatus()
        );
    }
}
