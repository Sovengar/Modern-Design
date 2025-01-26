package com.jonathan.modern_design.account.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountMoneyVO {
    private final BigDecimal amount;
    private final String currency;

    public static AccountMoneyVO of(BigDecimal amount, String currency) {
        return new AccountMoneyVO(amount, currency);
    }


    public AccountMoneyVO add(AccountMoneyVO other) {
        checkCurrency(other);
        return new AccountMoneyVO(this.amount.add(other.amount), this.currency);
    }

    public AccountMoneyVO subtract(AccountMoneyVO other) {
        checkCurrency(other);

        if (this.amount.compareTo(other.amount) < 0) {
            throw new IllegalArgumentException("Cannot subtract Money with bigger amount");
        }

        return new AccountMoneyVO(this.amount.subtract(other.amount), this.currency);
    }


    private void checkCurrency(AccountMoneyVO other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot operate Money with different currencies");
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


}
