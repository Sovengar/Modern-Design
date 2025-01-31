package com.jonathan.modern_design.account_module.domain.exceptions;

public class OperationWithDifferentCurrenciesException extends RuntimeException{
    public OperationWithDifferentCurrenciesException(){
        super("Operation with different currencies");
    }
}
