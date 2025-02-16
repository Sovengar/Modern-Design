package com.jonathan.modern_design.account_module.domain.model;

import com.jonathan.modern_design._infra.config.exception.RootException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static java.util.regex.Pattern.matches;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountNumber {
    private final String accountNumber;

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
        public InvalidAccountNumberException(String message) {
            super(message);
        }
    }
}
