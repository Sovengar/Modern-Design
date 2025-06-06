package jonathan.modern_design.account_module.domain.models.account.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jonathan.modern_design._common.tags.ValueObject;
import jonathan.modern_design._internal.config.exception.RootException;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.exceptions.OperationWithDifferentCurrenciesException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serial;
import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static lombok.AccessLevel.PRIVATE;

@Embeddable
@Value //No record for Hibernate
@NoArgsConstructor(access = PRIVATE, force = true) //For Hibernate
@AllArgsConstructor(staticName = "of")
public class AccountMoney implements ValueObject {
    BigDecimal balance;
    @Enumerated(value = EnumType.STRING)
    Currency currency;

    public AccountMoney add(AccountMoney other) {
        checkCurrency(other);
        assert this.balance != null;
        return new AccountMoney(this.balance.add(other.balance), this.currency);
    }

    public AccountMoney subtract(AccountMoney other) {
        checkCurrency(other);

        if (checkLowerThan(other.balance)) {
            throw new InsufficientFundsException();
        }

        assert this.balance != null;
        return new AccountMoney(this.balance.subtract(other.balance), this.currency);
    }

    private void checkCurrency(AccountMoney other) {
        if (this.currency == null || other == null || other.currency == null) {
            throw new OperationWithDifferentCurrenciesException();
        }

        if (!this.currency.equals(other.currency)) {
            throw new OperationWithDifferentCurrenciesException();
        }
    }

    public boolean checkPositiveOrZero() {
        if (this.balance == null) {
            return false;
        }
        return this.balance.compareTo(ZERO) >= 0;
    }

    public boolean checkNegative() {
        if (this.balance == null) {
            return false;
        }
        return this.balance.compareTo(ZERO) < 0;
    }

    public boolean checkPositive() {
        if (this.balance == null) {
            return false;
        }
        return this.balance.compareTo(ZERO) > 0;
    }

    public boolean checkGreaterThanOrEqualTo(BigDecimal anotherAmount) {
        if (this.balance == null || anotherAmount == null) {
            return false;
        }
        return this.balance.compareTo(anotherAmount) >= 0;
    }

    public boolean checkGreaterThan(BigDecimal anotherAmount) {
        if (this.balance == null || anotherAmount == null) {
            return false;
        }
        return this.balance.compareTo(anotherAmount) >= 1;
    }

    public boolean checkLowerThan(BigDecimal anotherAmount) {
        if (this.balance == null || anotherAmount == null) {
            return false;
        }
        return this.balance.compareTo(anotherAmount) < 0;
    }

    public static class InsufficientFundsException extends RootException {
        @Serial private static final long serialVersionUID = 4577125702505726581L;

        public InsufficientFundsException() {
            super("Account doesn't have enough money");
        }
    }
}
