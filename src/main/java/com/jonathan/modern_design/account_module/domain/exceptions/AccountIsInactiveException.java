package com.jonathan.modern_design.account_module.domain.exceptions;

import com.jonathan.modern_design._infra.config.exception.RootException;

public class AccountIsInactiveException extends RootException {
    public AccountIsInactiveException(String accountNumber) {
        super(String.format("Account %s is inactive", accountNumber));
    }
}
