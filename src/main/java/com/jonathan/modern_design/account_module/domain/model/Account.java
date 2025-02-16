package com.jonathan.modern_design.account_module.domain.model;

import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.user_module.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder //For Mapper and testing, otherwise use the create factory method.
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Account {
    Long id;
    String accountNumber;
    AccountMoney money;
    AccountAddress address;
    User user;
    LocalDateTime dateOfLastTransaction;
    boolean active;

    public static Account create(BigDecimal amount, Currency currency, String address, User user) {
        var accountNumber = UUID.randomUUID().toString(); //TODO CREAR CLASE QUE GENERE UN IBAN NO REPETIBLE
        LocalDateTime dateOfLastTransaction = null;
        var isActive = true;

        return new Account(null, accountNumber, AccountMoney.of(amount, currency), AccountAddress.of(address), user, dateOfLastTransaction, isActive);
    }

    public void add(BigDecimal amount, Currency currency) {
        this.money = this.money.add(AccountMoney.of(amount, currency));
        dateOfLastTransaction = LocalDateTime.now();
    }

    public void substract(BigDecimal amount, Currency currency) {
        this.money = this.money.substract(AccountMoney.of(amount, currency));
        dateOfLastTransaction = LocalDateTime.now();
    }
}
