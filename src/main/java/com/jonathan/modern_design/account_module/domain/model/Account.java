package com.jonathan.modern_design.account_module.domain.model;

import com.jonathan.modern_design.shared.Currency;
import com.jonathan.modern_design.user_module.User;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder //For Mapper and testing, otherwise use the create factory method.
public class Account {
    UUID uuid;
    AccountMoneyVO money;
    AddressVO address;
    User user;
    LocalDateTime dateOfLastTransaction;
    boolean isActive;

    private Account(UUID uuid, AccountMoneyVO money, AddressVO address, User user, LocalDateTime dateOfLastTransaction, boolean isActive) {
        this.uuid = uuid;
        this.money = money;
        this.user = user;
        this.address = address;
        this.dateOfLastTransaction = dateOfLastTransaction;
        this.isActive = isActive;
    }

    public static Account create(BigDecimal amount, Currency currency, String address, User user) {
        return new Account(UUID.randomUUID(), AccountMoneyVO.of(amount, currency), AddressVO.of(address), user, null, true);
    }

    public void deposit(BigDecimal amount, Currency currency) {
        this.money = this.money.deposit(AccountMoneyVO.of(amount, currency));
        dateOfLastTransaction = LocalDateTime.now();
    }

    public void substract(BigDecimal amount, Currency currency) {
        this.money = this.money.substract(AccountMoneyVO.of(amount, currency));
        dateOfLastTransaction = LocalDateTime.now();
    }

    public UUID getId() {
        return uuid;
    }

    public BigDecimal getAmount() {
        return money.getAmount();
    }

    public boolean isActive() {
        return isActive;
    }
}
