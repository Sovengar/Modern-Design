package com.jonathan.modern_design.account_module.domain.model;

import com.jonathan.modern_design._internal.config.exception.RootException;
import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.account_module.domain.exceptions.OperationWithDifferentCurrenciesException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountMoney {
    private final BigDecimal amount;
    private final Currency currency;

    public static AccountMoney of(BigDecimal amount, Currency currency) {
        return new AccountMoney(amount, currency);
    }

    public AccountMoney add(AccountMoney other) {
        checkCurrency(other);
        return new AccountMoney(this.amount.add(other.amount), this.currency);
    }

    public AccountMoney substract(AccountMoney other) {
        checkCurrency(other);

        if (isLowerThan(other.amount)) {
            throw new InsufficientFundsException();
        }

        return new AccountMoney(this.amount.subtract(other.amount), this.currency);
    }


    private void checkCurrency(AccountMoney other) {
        if (!this.currency.equals(other.currency)) {
            throw new OperationWithDifferentCurrenciesException();
        }
    }

    public boolean isPositiveOrZero() {
        return this.amount.compareTo(BigDecimal.ZERO) >= 0;
    }

    public boolean isNegative() {
        return this.amount.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isPositive() {
        return this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGreaterThanOrEqualTo(BigDecimal anotherAmount) {
        return this.amount.compareTo(anotherAmount) >= 0;
    }

    public boolean isGreaterThan(BigDecimal anotherAmount) {
        return this.amount.compareTo(anotherAmount) >= 1;
    }

    public boolean isLowerThan(BigDecimal anotherAmount) {
        return this.amount.compareTo(anotherAmount) < 0;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountMoney money = (AccountMoney) o;
        return amount.equals(money.amount) && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }


    public static class InsufficientFundsException extends RootException {
        @Serial private static final long serialVersionUID = 4577125702505726581L;

        public InsufficientFundsException() {
            super("Account doesnt have enough money");
        }
    }
}
