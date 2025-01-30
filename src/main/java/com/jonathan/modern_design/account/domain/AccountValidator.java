package com.jonathan.modern_design.account.domain;

import com.jonathan.modern_design.account.domain.exceptions.CannotDoOperationsException;
import com.jonathan.modern_design.account.domain.model.Account;

public class AccountValidator {

    public void validateAccount(Account account){
        validateActive(account);
    }

    private void validateActive(Account account) {
        if (!account.isActive()) {
            throw new CannotDoOperationsException(account.getId());
        }
    }
}

