package com.jonathan.modern_design.account.domain.exceptions;

import java.util.UUID;

public class AccountIsInactiveException extends RuntimeException {
    public AccountIsInactiveException(UUID accountId) {
        super(String.format("Account %s is inactive", accountId.toString()));
    }
}
