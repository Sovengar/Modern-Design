package com.jonathan.modern_design.account.domain.exceptions;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(){
        super("Account doesnt have enough money");
    }
}
