package com.jonathan.modern_design.account.domain.exceptions;

public class AccountNotFoundException extends RuntimeException {

   public AccountNotFoundException(Long id){
       super(String.format("Account with ID %d not found!", id));
   }
}
