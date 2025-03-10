package com.jonathan.modern_design.account_module.domain.model;

import com.jonathan.modern_design._shared.Currency;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.jonathan.modern_design.user_module.user.domain.model.User.UserId;

@Builder //For mapper and tests only
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Account {
    AccountId accountId;
    AccountNumber accountNumber;
    AccountMoney money;
    AccountAddress address;
    UserId userId;
    LocalDateTime dateOfLastTransaction;
    boolean active;

    public static Account create(AccountNumber accountNumber, AccountMoney money, AccountAddress address, UserId userId) {
        LocalDateTime dateOfLastTransaction = null;
        var isActive = true;

        return new Account(null, accountNumber, money, address, userId, dateOfLastTransaction, isActive);
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
