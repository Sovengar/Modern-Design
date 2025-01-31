package com.jonathan.modern_design.account.domain.exceptions;

import java.util.UUID;

public class AccountNotFoundException extends RuntimeException {

   public AccountNotFoundException(UUID id){
       super(String.format("Account with ID %s not found!", id.toString()));
   }
}
