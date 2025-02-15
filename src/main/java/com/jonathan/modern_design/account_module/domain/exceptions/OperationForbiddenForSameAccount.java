package com.jonathan.modern_design.account_module.domain.exceptions;

import com.jonathan.modern_design.config.exception.RootException;

public class OperationForbiddenForSameAccount extends RootException {
    public OperationForbiddenForSameAccount() {
        super("Operation forbidden for same account");
    }
}
