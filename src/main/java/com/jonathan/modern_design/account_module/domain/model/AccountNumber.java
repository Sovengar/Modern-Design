package com.jonathan.modern_design.account_module.domain.model;

import com.jonathan.modern_design._internal.config.exception.RootException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;

import static java.util.regex.Pattern.matches;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountNumber {
    private final String value;

    public static AccountNumber of(String accountNumber) {
        if (accountNumber == null || accountNumber.isEmpty()) {
            throw new InvalidAccountNumberException("Account number cannot be empty or null.");
        }

        if (accountNumber.length() <= 24) {
            throw new InvalidAccountNumberException("Account number must be more than 24 characters long.");
        }

        if (!matches(".*\\d.*", accountNumber)) {
            throw new InvalidAccountNumberException("Account number must contain at least one digit.");
        }

        return new AccountNumber(accountNumber);
    }

    private static class InvalidAccountNumberException extends RootException {
        @Serial private static final long serialVersionUID = 4910707570010059158L;

        public InvalidAccountNumberException(String message) {
            super(message);
        }
    }
}
