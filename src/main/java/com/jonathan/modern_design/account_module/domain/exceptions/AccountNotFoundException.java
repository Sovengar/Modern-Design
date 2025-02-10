package com.jonathan.modern_design.account_module.domain.exceptions;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String accountNumber) {
        super(String.format("Account with ID %s not found!", accountNumber));
    }
}
