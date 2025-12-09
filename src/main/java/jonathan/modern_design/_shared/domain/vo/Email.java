package jonathan.modern_design._shared.domain.vo;


import jakarta.persistence.Embeddable;
import jonathan.modern_design._config.exception.RootException;
import jonathan.modern_design._shared.tags.models.ValueObject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.util.StringUtils;

import java.io.Serial;

import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Embeddable
@ValueObject
@Value //No record for Hibernate
@NoArgsConstructor(access = PRIVATE, force = true) //For Hibernate
@AllArgsConstructor(access = PACKAGE) //Use factory method
public class Email {
    private static final int MAX_LENGTH = 254;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    String email;

    public static Email of(String email) {
        ofNullable(email)
                .filter(StringUtils::hasText)
                .orElseThrow(() -> new InvalidEmailException("Email cannot be null."));

        if (email.length() > MAX_LENGTH) {
            throw new InvalidEmailException("Email exceeds the maximum allowed length " + MAX_LENGTH);
        }

        if (!email.matches(EMAIL_REGEX)) {
            throw new InvalidEmailException("Invalid format for email");
        }

        return new Email(email.trim().toUpperCase());
    }

    static class InvalidEmailException extends RootException {
        @Serial private static final long serialVersionUID = 4728200511269608142L;

        public InvalidEmailException(String message) {
            super(message);
        }
    }
}
