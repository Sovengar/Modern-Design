package jonathan.modern_design.banking.domain.vo;

import jakarta.persistence.Embeddable;
import jonathan.modern_design._config.exception.RootException;
import jonathan.modern_design._shared.tags.ValueObject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serial;

import static java.util.regex.Pattern.matches;
import static lombok.AccessLevel.PRIVATE;

@Embeddable
@Value //No record for Hibernate
@NoArgsConstructor(access = PRIVATE, force = true) //For Hibernate
@AllArgsConstructor(access = PRIVATE)
public class AccountNumber implements ValueObject {
    String accountNumber;

    public static AccountNumber of(String accountNumber) {
        if (accountNumber == null || accountNumber.isEmpty()) {
            throw new InvalidAccountNumberException("Account number cannot be empty or null.");
        }

        if (accountNumber.length() <= 24) {
            throw new InvalidAccountNumberException("Account number must be more than 24 characters long.");
        }

        if (!matches(".*\\d.*", accountNumber)) {
            throw new InvalidAccountNumberException("Account number must contain at least one digit.");
        }

        return new AccountNumber(accountNumber);
    }

    private static class InvalidAccountNumberException extends RootException {
        @Serial private static final long serialVersionUID = 4910707570010059158L;

        public InvalidAccountNumberException(String message) {
            super(message);
        }
    }
}
