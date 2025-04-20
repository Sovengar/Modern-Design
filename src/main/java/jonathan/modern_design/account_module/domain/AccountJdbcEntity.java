package jonathan.modern_design.account_module.domain;

import jakarta.persistence.Id;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.domain.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.vo.AccountId;
import jonathan.modern_design.account_module.domain.vo.AccountMoney;
import jonathan.modern_design.user.domain.User;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Table("md.accounts")
public record AccountJdbcEntity(
        @Id Long id,
        String accountNumber,
        BigDecimal balance,
        Currency currency,
        String address,
        LocalDateTime dateOfLastTransaction,
        boolean active,
        UUID userId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public AccountJdbcEntity(AccountJdbcEntity entity, Account account) {
        this(
                account.accountId().id(),
                account.accountAccountNumber().accountNumber(),
                account.money().amount(),
                account.money().currency(),
                account.address().toString(),
                account.dateOfLastTransaction(),
                account.active(),
                account.userId().userUuid(),
                entity.createdAt(),
                entity.updatedAt()
        );
    }

    public Account toDomain() {
        return new Account(
                new AccountId(id),
                AccountAccountNumber.of(accountNumber),
                AccountMoney.of(balance, currency),
                AccountAddress.of(address),
                new User.UserId(userId),
                dateOfLastTransaction,
                active
        );
    }
}
