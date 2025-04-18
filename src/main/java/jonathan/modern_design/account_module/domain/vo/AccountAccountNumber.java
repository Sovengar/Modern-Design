package jonathan.modern_design.account_module.domain.vo;

import jakarta.persistence.Embeddable;
import jonathan.modern_design._internal.config.exception.RootException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serial;

import static java.util.regex.Pattern.matches;

@Embeddable
@Value //No record for Hibernate
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) //For Hibernate
public class AccountAccountNumber {
    String accountNumber;

    public static AccountAccountNumber of(String accountNumber) {
        if (accountNumber == null || accountNumber.isEmpty()) {
            throw new InvalidAccountNumberException("Account number cannot be empty or null.");
        }

        if (accountNumber.length() <= 24) {
            throw new InvalidAccountNumberException("Account number must be more than 24 characters long.");
        }

        if (!matches(".*\\d.*", accountNumber)) {
            throw new InvalidAccountNumberException("Account number must contain at least one digit.");
        }

        return new AccountAccountNumber(accountNumber);
    }

    private static class InvalidAccountNumberException extends RootException {
        @Serial private static final long serialVersionUID = 4910707570010059158L;

        public InvalidAccountNumberException(String message) {
            super(message);
        }
    }
}
