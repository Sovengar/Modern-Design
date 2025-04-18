package jonathan.modern_design.account_module.infra;

import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.AccountEntity;
import jonathan.modern_design.user.domain.User.UserId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountDto(
        String accountNumber,
        BigDecimal balance,
        String currency,
        String address,
        LocalDateTime dateOfLastTransaction,
        boolean active,
        UserId userId) {

    public AccountDto(final Account account) {
        this(
                account.accountAccountNumber().accountNumber(),
                account.money().amount(),
                account.money().currency().description(),
                account.address().toString(),
                account.dateOfLastTransaction(),
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
                account.dateOfLastTransaction(),
                account.active(),
                account.userId()
        );
    }
}
