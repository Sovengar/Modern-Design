package com.jonathan.modern_design.user_module.model;

import com.jonathan.modern_design._infra.config.exception.RootException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRealName {
    private final String name;

    public static UserRealName of(String name) {
        if (name.matches(".*\\d.*")) {
            throw new UserRealNameNotValidException("Your name cannot contain numbers.");
        }

        return new UserRealName(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRealName nameVO)) return false;
        return name.equals(nameVO.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String getName() {
        return name;
    }

    private static class UserRealNameNotValidException extends RootException {
        public UserRealNameNotValidException(String message) {
            super(message);
        }
    }
}
