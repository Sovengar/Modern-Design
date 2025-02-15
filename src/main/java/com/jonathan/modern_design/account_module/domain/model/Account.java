package com.jonathan.modern_design.account_module.domain.model;

import com.jonathan.modern_design.shared.Currency;
import com.jonathan.modern_design.user_module.User;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder //For Mapper and testing, otherwise use the create factory method.
public class Account {

    Long id;
    String accountNumber;
    AccountMoneyVO money;
    AccountAddressVO address;
    User user;
    LocalDateTime dateOfLastTransaction;
    boolean active;

    private Account(Long id, String accountNumber, AccountMoneyVO money, AccountAddressVO address, User user, LocalDateTime dateOfLastTransaction, boolean active) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.money = money;
        this.user = user;
        this.address = address;
        this.dateOfLastTransaction = dateOfLastTransaction;
        this.active = active;
    }

    public static Account create(BigDecimal amount, Currency currency, String address, User user) {
        var accountNumber = UUID.randomUUID().toString(); //TODO CREAR CLASE QUE GENERE UN IBAN NO REPETIBLE
        return new Account(null, accountNumber, AccountMoneyVO.of(amount, currency), AccountAddressVO.of(address), user, null, true);
    }

    public void add(BigDecimal amount, Currency currency) {
        this.money = this.money.add(AccountMoneyVO.of(amount, currency));
        dateOfLastTransaction = LocalDateTime.now();
    }

    public void substract(BigDecimal amount, Currency currency) {
        this.money = this.money.substract(AccountMoneyVO.of(amount, currency));
        dateOfLastTransaction = LocalDateTime.now();
    }

    //GETTERS
    public Long getId() {
        return id;
    }

    public User getUser() { //Only use it to map, dont use it for anything else
        return user;
    }

    public LocalDateTime getDateOfLastTransaction() {
        return dateOfLastTransaction;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAddress() {
        return address.toString();
    }

    public AccountMoneyVO getMoney() {
        return money;
    }

    public boolean isActive() {
        return active;
    }
}
