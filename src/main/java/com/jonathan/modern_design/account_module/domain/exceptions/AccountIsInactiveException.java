package com.jonathan.modern_design.account_module.domain.exceptions;

import java.util.UUID;

public class AccountIsInactiveException extends RuntimeException {
    public AccountIsInactiveException(UUID accountId) {
        super(String.format("Account %s is inactive", accountId.toString()));
    }
}
