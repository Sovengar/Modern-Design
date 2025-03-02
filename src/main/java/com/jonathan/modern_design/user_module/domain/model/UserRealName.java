package com.jonathan.modern_design.user_module.domain.model;

import com.jonathan.modern_design._infra.config.exception.RootException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRealName {
    private final String value;

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
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRealName nameVO)) return false;
        return value.equals(nameVO.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public Optional<String> getValue() {
        return ofNullable(value);
    }

    private static class UserRealNameNotValidException extends RootException {
        public UserRealNameNotValidException(String message) {
            super(message);
        }
    }
}
