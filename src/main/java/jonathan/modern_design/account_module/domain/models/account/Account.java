package jonathan.modern_design.account_module.domain.models.account;

import jonathan.modern_design._common.annotations.AggregateRoot;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.exceptions.AccountIsInactiveException;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountId;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountMoney;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;
import static jonathan.modern_design.user.domain.User.UserId;

@Builder //For mapper and tests only
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AggregateRoot
public final class Account {
    AccountId accountId;
    AccountAccountNumber accountAccountNumber;
    AccountMoney money;
    AccountAddress address;
    UserId userId;
    LocalDateTime dateOfLastTransaction;
    boolean active;

    //TODO THIS MAKES 0 SENSE, EXTRACT FIELDS THAT HAS NO LOGIC ASSOCIATED
    public Account(AccountEntity accountEntity) {
        this.accountId = new AccountId(accountEntity.accountId());
        this.accountAccountNumber = AccountAccountNumber.of(accountEntity.accountNumber());
        this.money = AccountMoney.of(accountEntity.balance(), accountEntity.currency());
        this.address = AccountAddress.of(accountEntity.address());
        this.userId = accountEntity.userId();
        this.dateOfLastTransaction = accountEntity.dateOfLastTransaction();
        this.active = accountEntity.active();
    }

    public static Account create(AccountAccountNumber accountAccountNumber, AccountMoney money, AccountAddress address, UserId userId) {
        LocalDateTime dateOfLastTransaction = null;
        var isActive = true;

        return new Account(
                null,
                requireNonNull(accountAccountNumber),
                requireNonNull(money),
                requireNonNull(address),
                requireNonNull(userId),
                dateOfLastTransaction,
                isActive);
    }

    //TODO USE THE ENTITY CONSTRUCTOR?
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

    public void deactivate() {
        if (!this.active) throw new AccountIsInactiveException(this.accountAccountNumber.accountNumber());
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }
}
