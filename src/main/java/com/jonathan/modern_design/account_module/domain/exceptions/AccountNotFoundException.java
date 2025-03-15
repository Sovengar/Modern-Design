package com.jonathan.modern_design.account_module.domain.exceptions;

import com.jonathan.modern_design._internal.config.exception.RootException;

import java.io.Serial;

public class AccountNotFoundException extends RootException {

    @Serial private static final long serialVersionUID = -3171077750682245887L;

    public AccountNotFoundException(String accountNumber) {
        super(String.format("Account with ID %s not found!", accountNumber));
    }
}
