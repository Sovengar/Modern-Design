package com.jonathan.modern_design.account_module.domain.exceptions;

import com.jonathan.modern_design.config.exception.RootException;

public class OperationWithDifferentCurrenciesException extends RootException {
    public OperationWithDifferentCurrenciesException() {
        super("Operation with different currencies");
    }
}
