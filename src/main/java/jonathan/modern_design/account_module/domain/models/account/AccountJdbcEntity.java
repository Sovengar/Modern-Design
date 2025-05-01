package jonathan.modern_design.account_module.domain.models.account;

import jakarta.persistence.Id;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountMoney;
import jonathan.modern_design.user.domain.models.User;
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
        boolean active,
        UUID userId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public AccountJdbcEntity(AccountJdbcEntity entity, Account account) {
        this(
                account.accountId().id(),
                account.accountAccountNumber().accountNumber(),
                account.money().balance(),
                account.money().currency(),
                account.address().toString(),
                account.active(),
                account.userId().userId(),
                entity.createdAt(),
                entity.updatedAt()
        );
    }

    public Account toDomain() {
        return new Account(
                new Account.Id(id),
                AccountAccountNumber.of(accountNumber),
                AccountMoney.of(balance, currency),
                AccountAddress.of(address),
                User.Id.of(userId),
                active
        );
    }
}
