package com.jonathan.modern_design.account_module.domain.exceptions;

import com.jonathan.modern_design.config.exception.RootException;

public class AccountNotFoundException extends RootException {

    public AccountNotFoundException(String accountNumber) {
        super(String.format("Account with ID %s not found!", accountNumber));
    }
}
