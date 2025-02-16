package com.jonathan.modern_design.user_module.domain.model;

import com.jonathan.modern_design._infra.config.exception.RootException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserEmail {
    private static final int MAX_LENGTH = 254;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private final String email;

    public static UserEmail of(String email) {
        if (email == null) {
            throw new InvalidEmailException("El email no puede ser nulo.");
        }
        if (email.length() > MAX_LENGTH) {
            throw new InvalidEmailException("El email excede la longitud máxima permitida de " + MAX_LENGTH + " caracteres.");
        }
        if (!email.matches(EMAIL_REGEX)) {
            throw new InvalidEmailException("Formato de email inválido.");
        }

        return new UserEmail(email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEmail emailVO)) return false;
        return email.equals(emailVO.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public String toString() {
        return email;
    }

    private static class InvalidEmailException extends RootException {
        public InvalidEmailException(String message) {
            super(message);
        }
    }
}
