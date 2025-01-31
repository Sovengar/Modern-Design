package com.jonathan.modern_design.account_module.domain.exceptions;

public class OperationForbiddenForSameAccount extends RuntimeException {
    public OperationForbiddenForSameAccount() {
        super("Operation forbidden for same account");
    }
}
