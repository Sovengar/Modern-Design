package com.jonathan.modern_design.account.domain.exceptions;

public class CannotDoOperationsException extends RuntimeException {
    public CannotDoOperationsException(Long id) {
        super(String.format("Account with ID %d cannot do operations", id));
    }
}
