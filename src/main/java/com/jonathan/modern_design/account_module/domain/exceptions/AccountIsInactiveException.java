package com.jonathan.modern_design.account_module.domain.exceptions;

public class AccountIsInactiveException extends RuntimeException {
    public AccountIsInactiveException(String accountNumber) {
        super(String.format("Account %s is inactive", accountNumber));
    }
}
