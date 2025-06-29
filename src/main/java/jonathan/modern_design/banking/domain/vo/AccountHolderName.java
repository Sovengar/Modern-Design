package jonathan.modern_design.banking.domain.vo;

import jakarta.persistence.Embeddable;
import jonathan.modern_design._config.exception.RootException;
import jonathan.modern_design._shared.tags.ValueObject;
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
public class AccountHolderName implements ValueObject {
    String name;

    public static AccountHolderName of(Optional<String> nameOpt) {
        if (nameOpt.isEmpty()) {
            return new AccountHolderName(null);
        }

        var name = nameOpt.get();
        if (name.matches(".*\\d.*")) {
            throw new UserRealNameNotValidException("Your name cannot contain numbers.");
        }

        return new AccountHolderName(name);
    }

    public Optional<String> getOptionalName() {
        return ofNullable(name);
    }

    private static class UserRealNameNotValidException extends RootException {
        @Serial private static final long serialVersionUID = -6239696180800018814L;

        public UserRealNameNotValidException(String message) {
            super(message);
        }
    }
}
