package com.jonathan.modern_design.account_module.domain.exceptions;

import com.jonathan.modern_design._internal.config.exception.RootException;

import java.io.Serial;

public class OperationWithDifferentCurrenciesException extends RootException {
    @Serial private static final long serialVersionUID = 8611756717306849952L;

    public OperationWithDifferentCurrenciesException() {
        super("Operation with different currencies");
    }
}
