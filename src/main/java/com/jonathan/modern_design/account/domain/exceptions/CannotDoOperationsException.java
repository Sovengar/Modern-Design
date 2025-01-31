package com.jonathan.modern_design.account.domain.exceptions;

import java.util.UUID;

public class CannotDoOperationsException extends RuntimeException {
    public CannotDoOperationsException(UUID id) {
        super(String.format("Account with ID %s cannot do operations", id.toString()));
    }
}
