package jonathan.modern_design.user.domain.vo;

import jakarta.persistence.Embeddable;
import jonathan.modern_design._internal.config.exception.RootException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serial;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Embeddable
@Value //No record for Hibernate
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) //For Hibernate
public class UserRealName {
    String realname;

    public static UserRealName of(String name) {
        if (name == null || name.isBlank()) {
            return new UserRealName(null);
        }

        if (name.matches(".*\\d.*")) {
            throw new UserRealNameNotValidException("Your name cannot contain numbers.");
        }

        return new UserRealName(name);
    }

    public Optional<String> getRealname() {
        return ofNullable(realname);
    }

    private static class UserRealNameNotValidException extends RootException {
        @Serial private static final long serialVersionUID = -6239696180800018814L;

        public UserRealNameNotValidException(String message) {
            super(message);
        }
    }
}
