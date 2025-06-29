package jonathan.modern_design.auth.domain.vo;


import jakarta.persistence.Embeddable;
import jonathan.modern_design._config.exception.RootException;
import jonathan.modern_design._shared.tags.ValueObject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.util.StringUtils;

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
        if (!StringUtils.hasText(email)) {
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
