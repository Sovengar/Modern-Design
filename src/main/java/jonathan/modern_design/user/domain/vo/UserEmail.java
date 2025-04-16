package jonathan.modern_design.user.domain.vo;


import jakarta.persistence.Embeddable;
import jonathan.modern_design._internal.config.exception.RootException;

import java.io.Serial;

@Embeddable
public record UserEmail(String email) {
    private static final int MAX_LENGTH = 254;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

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
}

class InvalidEmailException extends RootException {
    @Serial private static final long serialVersionUID = 4728200511269608142L;

    public InvalidEmailException(String message) {
        super(message);
    }
}
