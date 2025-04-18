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

        return new Account(null, accountAccountNumber, money, address, userId, dateOfLastTransaction, isActive);
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
}
