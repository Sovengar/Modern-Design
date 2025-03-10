package com.jonathan.modern_design.user_module.user.domain.model;

import com.jonathan.modern_design._infra.config.exception.RootException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static java.util.regex.Pattern.matches;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserPassword {
    private String password;

    public static UserPassword of(String password) {
        PasswordValidator.validatePassword(password);

        return new UserPassword(password);
    }

    @Override
    public String toString() {
        return password;
    }

    @Override
    public int hashCode() {
        return password.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPassword passwordVO)) return false;
        return password.equals(passwordVO.password);
    }

    private static class InvalidUserPasswordException extends RootException {
        public InvalidUserPasswordException(String message) {
            super(message);
        }
    }

    private static class PasswordValidator {
        private static final int MIN_LENGTH = 8;
        private static final int MAX_LENGTH = 50;
        private static final Map<Predicate<String>, String> errorMessages = new HashMap<>();
        private static final List<Predicate<String>> predicates = Arrays.asList(
                notEmpty(),
                minLength(),
                maxLength(),
                containsDigit(),
                containsUppercase(),
                containsLowercase(),
                containsSpecialCharacter()
        );

        static {
            errorMessages.put(notEmpty(), "Password cannot be empty or null.");
            errorMessages.put(minLength(), "Password does not meet the minimum length of " + MIN_LENGTH + " characters.");
            errorMessages.put(maxLength(), "Password does not meet the maximum length of " + MAX_LENGTH + " characters.");
            errorMessages.put(containsDigit(), "Password must contain at least one digit.");
            errorMessages.put(containsUppercase(), "Password must contain at least one uppercase letter.");
            errorMessages.put(containsLowercase(), "Password must contain at least one lowercase letter.");
            errorMessages.put(containsSpecialCharacter(), "Password must contain at least one special character.");
        }

        public static void validatePassword(String password) {
            predicates.stream()
                    .filter(predicate -> !predicate.test(password))
                    .findFirst()
                    .ifPresent(predicate -> {
                        var errorMessage = errorMessages.get(predicate);
                        throw new InvalidUserPasswordException(errorMessage);
                    });
        }

        private static Predicate<String> notEmpty() {
            return password -> password != null && !password.isEmpty();
        }

        private static Predicate<String> minLength() {
            return password -> password.length() >= MIN_LENGTH;
        }

        private static Predicate<String> maxLength() {
            return password -> password.length() <= MAX_LENGTH;
        }

        private static Predicate<String> containsDigit() {
            return password -> matches(".*\\d.*", password);
        }

        private static Predicate<String> containsUppercase() {
            return password -> matches(".*[A-Z].*", password);
        }

        private static Predicate<String> containsLowercase() {
            return password -> matches(".*[a-z].*", password);
        }

        private static Predicate<String> containsSpecialCharacter() {
            return password -> matches(".*\\W.*", password);
        }
    }
}
