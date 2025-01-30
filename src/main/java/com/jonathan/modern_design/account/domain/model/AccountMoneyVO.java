package com.jonathan.modern_design.account.domain.model;

import com.jonathan.modern_design.account.domain.exceptions.InsufficientFundsException;
import com.jonathan.modern_design.account.domain.exceptions.OperationWithDifferentCurrenciesException;
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


    public AccountMoneyVO add(AccountMoneyVO other) {
        checkCurrency(other);
        return new AccountMoneyVO(this.amount.add(other.amount), this.currency);
    }

    public AccountMoneyVO substract(AccountMoneyVO other) {
        checkCurrency(other);

        if (this.amount.compareTo(other.amount) < 0) {
            throw new InsufficientFundsException();
        }

        return new AccountMoneyVO(this.amount.subtract(other.amount), this.currency);
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


}
