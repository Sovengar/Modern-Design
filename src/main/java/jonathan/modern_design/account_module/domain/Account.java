package jonathan.modern_design.account_module.domain;

import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.domain.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.vo.AccountId;
import jonathan.modern_design.account_module.domain.vo.AccountMoney;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static java.util.Objects.nonNull;
import static jonathan.modern_design.user.domain.User.UserId;

@Builder //For mapper and tests only
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public final class Account {
    AccountId accountId;
    AccountAccountNumber accountAccountNumber;
    AccountMoney money;
    AccountAddress address;
    UserId userId;
    LocalDateTime dateOfLastTransaction;
    boolean active;

    public static Account create(AccountAccountNumber accountAccountNumber, AccountMoney money, AccountAddress address, UserId userId) {
        LocalDateTime dateOfLastTransaction = null;
        var isActive = true;

        return new Account(null, accountAccountNumber, money, address, userId, dateOfLastTransaction, isActive);
    }

    public static Account updateCRUD(Account account, AccountAccountNumber accountAccountNumber, AccountMoney money, AccountAddress address, boolean isActive, UserId userId) {
        return new Account(account.accountId(), accountAccountNumber, money, address, userId, account.dateOfLastTransaction(), isActive);
    }

    public void add(BigDecimal amount, Currency currency) {
        this.money = this.money.add(AccountMoney.of(amount, currency));
        dateOfLastTransaction = LocalDateTime.now();
    }

    public void subtract(BigDecimal amount, Currency currency) {
        this.money = this.money.substract(AccountMoney.of(amount, currency));
        dateOfLastTransaction = LocalDateTime.now();
    }

    public AccountEntity toEntity() {
        return new AccountEntity(
                nonNull(accountId) ? accountId.id() : null,
                accountAccountNumber.accountNumber(),
                money.amount(),
                money.currency(),
                address.toString(),
                dateOfLastTransaction,
                active,
                userId
        );
    }
}
