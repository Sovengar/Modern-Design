package com.jonathan.modern_design.account_module.domain.exceptions;

import java.util.UUID;

public class CannotDoOperationsException extends RuntimeException {
    public CannotDoOperationsException(UUID id) {
        super(String.format("Account with ID %s cannot do operations", id.toString()));
    }
}
