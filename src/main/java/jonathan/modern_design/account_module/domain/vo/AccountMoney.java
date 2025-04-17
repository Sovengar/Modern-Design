package jonathan.modern_design.account_module.domain.vo;

import com.google.common.annotations.VisibleForTesting;
import jakarta.persistence.Embeddable;
import jonathan.modern_design._internal.config.exception.RootException;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.exceptions.OperationWithDifferentCurrenciesException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serial;
import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

@Embeddable
@Value //No record for Hibernate
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) //For Hibernate
public class AccountMoney {
    BigDecimal amount;
    Currency currency;

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
        assert this.currency != null;
        if (!this.currency.equals(other.currency)) {
            throw new OperationWithDifferentCurrenciesException();
        }
    }

    public boolean isPositiveOrZero() {
        return this.amount.compareTo(ZERO) >= 0;
    }

    public boolean isNegative() {
        return this.amount.compareTo(ZERO) < 0;
    }

    public boolean isPositive() {
        return this.amount.compareTo(ZERO) > 0;
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

    @VisibleForTesting //TODO QUE FUNCIONE
    public static class InsufficientFundsException extends RootException {
        @Serial private static final long serialVersionUID = 4577125702505726581L;

        public InsufficientFundsException() {
            super("Account doesnt have enough money");
        }
    }
}
