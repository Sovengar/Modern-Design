package com.jonathan.modern_design.account.domain.exceptions;

public class OperationForbiddenForSameAccount extends RuntimeException {
    public OperationForbiddenForSameAccount() {
        super("Operation forbidden for same account");
    }
}
