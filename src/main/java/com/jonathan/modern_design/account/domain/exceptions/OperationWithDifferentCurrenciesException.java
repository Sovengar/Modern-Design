package com.jonathan.modern_design.account.domain.exceptions;

public class OperationWithDifferentCurrenciesException extends RuntimeException{
    public OperationWithDifferentCurrenciesException(){
        super("Operation with different currencies");
    }
}
