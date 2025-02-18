package com.jonathan.modern_design.user_module.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserPasswordVO {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 50;
    private final String password;

    public static UserPasswordVO of(String password) {
        if (password.length() < MIN_LENGTH) {
            throw new UserPasswordNotValidException("Password does not meet the minimum length of " + MIN_LENGTH + " characters.");
        }

        if (password.length() > MAX_LENGTH) {
            throw new UserPasswordNotValidException("Password does not meet the maximum length of " + MAX_LENGTH + " characters.");
        }

        return new UserPasswordVO(password);
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
        if (!(o instanceof UserPasswordVO passwordVO)) return false;
        return password.equals(passwordVO.password);
    }

    private static class UserPasswordNotValidException extends RuntimeException {
        public UserPasswordNotValidException(String message) {
            super(message);
        }
    }
}
