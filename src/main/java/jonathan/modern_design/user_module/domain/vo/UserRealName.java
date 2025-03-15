package jonathan.modern_design.user_module.domain.vo;

import jakarta.persistence.Embeddable;
import jonathan.modern_design._internal.config.exception.RootException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Embeddable
public class UserRealName {
    private String realname;

    public static UserRealName of(String name) {
        if (name == null || name.isBlank()) {
            return new UserRealName(null);
        }

        if (name.matches(".*\\d.*")) {
            throw new UserRealNameNotValidException("Your name cannot contain numbers.");
        }

        return new UserRealName(name);
    }

    @Override
    public String toString() {
        return realname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRealName nameVO)) return false;
        return realname.equals(nameVO.realname);
    }

    @Override
    public int hashCode() {
        return realname.hashCode();
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
