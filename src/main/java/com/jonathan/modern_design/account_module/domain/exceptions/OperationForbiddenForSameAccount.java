package com.jonathan.modern_design.account_module.domain.exceptions;

import com.jonathan.modern_design._internal.config.exception.RootException;

import java.io.Serial;

public class OperationForbiddenForSameAccount extends RootException {
    @Serial private static final long serialVersionUID = 809998136604950305L;

    public OperationForbiddenForSameAccount() {
        super("Operation forbidden for same account");
    }
}
