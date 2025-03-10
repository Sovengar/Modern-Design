package com.jonathan.modern_design.user_module.user.domain.model;

import com.jonathan.modern_design._infra.config.exception.RootException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Embeddable
public class UserEmail {
    private static final int MAX_LENGTH = 254;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private String value;

    public static UserEmail of(String email) {
        if (email == null || email.isEmpty() || email.isBlank()) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEmail emailVO)) return false;
        return value.equals(emailVO.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }

    private static class InvalidEmailException extends RootException {
        public InvalidEmailException(String message) {
            super(message);
        }
    }
}
