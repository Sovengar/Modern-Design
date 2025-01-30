package com.jonathan.modern_design.account.domain.model;

import com.jonathan.modern_design.common.Currency;
import com.jonathan.modern_design.user_module.domain.model.User;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder //For Mapper and testing, otherwise use the create factory method.
public class Account {
    AccountId id;
    AccountMoneyVO money;
    User user;
    LocalDateTime dateOfLastTransaction;
    boolean isActive;

    private Account(AccountId id, AccountMoneyVO money, User user, LocalDateTime dateOfLastTransaction, boolean isActive) {
        this.id = id;
        this.money = money;
        this.user = user;
        this.dateOfLastTransaction = dateOfLastTransaction;
        this.isActive = isActive;
    }

    public static Account create(Long id, BigDecimal amount, Currency currency, User user) {
        return new Account(AccountId.of(id), AccountMoneyVO.of(amount, currency), user, null, true);
    }

    public boolean isBalanceGreaterThan(BigDecimal anotherAmount) {
        return this.money.getAmount().compareTo(anotherAmount) >= 0;
    }

    public void add(BigDecimal amount, Currency currency) {
        this.money = this.money.add(AccountMoneyVO.of(amount, currency));
        dateOfLastTransaction = LocalDateTime.now();
    }

    public void substract(BigDecimal amount, Currency currency) {
        this.money = this.money.substract(AccountMoneyVO.of(amount, currency));
        dateOfLastTransaction = LocalDateTime.now();
    }

    public Long getId(){
        return id.getValue();
    }

    public BigDecimal getAmount() {
        return money.getAmount();
    }

    public boolean isActive() {
        return isActive;
    }
}
