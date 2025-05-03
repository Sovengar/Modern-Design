package jonathan.modern_design.user.domain.models.vo;


import jakarta.persistence.Embeddable;
import jonathan.modern_design._common.annotations.ValueObject;
import jonathan.modern_design._internal.config.exception.RootException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serial;

import static lombok.AccessLevel.PRIVATE;

@Embeddable
@Value //No record for Hibernate
@NoArgsConstructor(access = PRIVATE, force = true)
@AllArgsConstructor(access = PRIVATE)
public class UserEmail implements ValueObject {
    private static final int MAX_LENGTH = 254;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    String email;

    public static UserEmail of(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidEmailException("Email cannot be null.");
        }

        if (email.length() > MAX_LENGTH) {
            throw new InvalidEmailException("Email exceeds the maximum allowed length " + MAX_LENGTH);
        }

        if (!email.matches(EMAIL_REGEX)) {
            throw new InvalidEmailException("Invalid format for email");
        }

        return new UserEmail(email);
    }

    static class InvalidEmailException extends RootException {
        @Serial private static final long serialVersionUID = 4728200511269608142L;

        public InvalidEmailException(String message) {
            super(message);
        }
    }
}
