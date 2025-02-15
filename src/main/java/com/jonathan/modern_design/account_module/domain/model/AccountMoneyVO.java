package com.jonathan.modern_design.account_module.domain.model;

import com.jonathan.modern_design.account_module.domain.exceptions.OperationWithDifferentCurrenciesException;
import com.jonathan.modern_design.config.exception.RootException;
import com.jonathan.modern_design.shared.Currency;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountMoneyVO {
    private final BigDecimal balance;
    private final Currency currency;

    public static AccountMoneyVO of(BigDecimal amount, Currency currency) {
        return new AccountMoneyVO(amount, currency);
    }

    public AccountMoneyVO add(AccountMoneyVO other) {
        checkCurrency(other);
        return new AccountMoneyVO(this.balance.add(other.balance), this.currency);
    }

    public AccountMoneyVO substract(AccountMoneyVO other) {
        checkCurrency(other);

        if (isBalanceLowerThan(other.balance)) {
            throw new InsufficientFundsException();
        }

        return new AccountMoneyVO(this.balance.subtract(other.balance), this.currency);
    }

    public boolean isBalanceLowerThan(BigDecimal anotherAmount) {
        return this.balance.compareTo(anotherAmount) < 0;
    }

    private void checkCurrency(AccountMoneyVO other) {
        if (!this.currency.equals(other.currency)) {
            throw new OperationWithDifferentCurrenciesException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountMoneyVO money = (AccountMoneyVO) o;
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
