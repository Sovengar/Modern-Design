package com.jonathan.modern_design.account_module.domain.model;

import com.jonathan.modern_design._infra.config.exception.RootException;
import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.account_module.domain.exceptions.OperationWithDifferentCurrenciesException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountMoney {
    private final BigDecimal balance;
    private final Currency currency;

    public static AccountMoney of(BigDecimal amount, Currency currency) {
        return new AccountMoney(amount, currency);
    }

    public AccountMoney add(AccountMoney other) {
        checkCurrency(other);
        return new AccountMoney(this.balance.add(other.balance), this.currency);
    }

    public AccountMoney substract(AccountMoney other) {
        checkCurrency(other);

        if (isBalanceLowerThan(other.balance)) {
            throw new InsufficientFundsException();
        }

        return new AccountMoney(this.balance.subtract(other.balance), this.currency);
    }

    public boolean isBalanceLowerThan(BigDecimal anotherAmount) {
        return this.balance.compareTo(anotherAmount) < 0;
    }

    private void checkCurrency(AccountMoney other) {
        if (!this.currency.equals(other.currency)) {
            throw new OperationWithDifferentCurrenciesException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountMoney money = (AccountMoney) o;
        return balance.equals(money.balance) && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance, currency);
    }

    @Override
    public String toString() {
        return balance + " " + currency;
    }


    public static class InsufficientFundsException extends RootException {
        public InsufficientFundsException() {
            super("Account doesnt have enough money");
        }
    }
}
