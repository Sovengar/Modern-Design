package jonathan.modern_design.account_module.domain.models.account.vo;

import com.google.common.annotations.VisibleForTesting;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class AccountMoney {
    BigDecimal balance;
    @Enumerated(value = EnumType.STRING)
    Currency currency;

    public AccountMoney add(AccountMoney other) {
        checkCurrency(other);
        return new AccountMoney(this.balance.add(other.balance), this.currency);
    }

    public AccountMoney subtract(AccountMoney other) {
        checkCurrency(other);

        if (isLowerThan(other.balance)) {
            throw new InsufficientFundsException();
        }

        return new AccountMoney(this.balance.subtract(other.balance), this.currency);
    }

    private void checkCurrency(AccountMoney other) {
        assert this.currency != null;
        if (!this.currency.equals(other.currency)) {
            throw new OperationWithDifferentCurrenciesException();
        }
    }

    public boolean isPositiveOrZero() {
        return this.balance.compareTo(ZERO) >= 0;
    }

    public boolean isNegative() {
        return this.balance.compareTo(ZERO) < 0;
    }

    public boolean isPositive() {
        return this.balance.compareTo(ZERO) > 0;
    }

    public boolean isGreaterThanOrEqualTo(BigDecimal anotherAmount) {
        return this.balance.compareTo(anotherAmount) >= 0;
    }

    public boolean isGreaterThan(BigDecimal anotherAmount) {
        return this.balance.compareTo(anotherAmount) >= 1;
    }

    public boolean isLowerThan(BigDecimal anotherAmount) {
        return this.balance.compareTo(anotherAmount) < 0;
    }

    @VisibleForTesting //TODO QUE FUNCIONE
    public static class InsufficientFundsException extends RootException {
        @Serial private static final long serialVersionUID = 4577125702505726581L;

        public InsufficientFundsException() {
            super("Account doesn't have enough money");
        }
    }
}
