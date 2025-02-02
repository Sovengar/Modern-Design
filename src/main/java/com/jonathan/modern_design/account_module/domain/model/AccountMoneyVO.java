package com.jonathan.modern_design.account_module.domain.model;

import com.jonathan.modern_design.account_module.domain.exceptions.OperationWithDifferentCurrenciesException;
import com.jonathan.modern_design.common.Currency;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountMoneyVO {
    private final BigDecimal amount;
    private final Currency currency;

    public static AccountMoneyVO of(BigDecimal amount, Currency currency) {
        return new AccountMoneyVO(amount, currency);
    }

    public AccountMoneyVO deposit(AccountMoneyVO other) {
        checkCurrency(other);
        return new AccountMoneyVO(this.amount.add(other.amount), this.currency);
    }

    public AccountMoneyVO substract(AccountMoneyVO other) {
        checkCurrency(other);

        if (isBalanceLowerThan(other.amount)) {
            throw new InsufficientFundsException();
        }

        return new AccountMoneyVO(this.amount.subtract(other.amount), this.currency);
    }

    public boolean isBalanceLowerThan(BigDecimal anotherAmount) {
        return this.amount.compareTo(anotherAmount) < 0;
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


    public static class InsufficientFundsException extends RuntimeException{
        public InsufficientFundsException(){
            super("Account doesnt have enough money");
        }
    }
}
