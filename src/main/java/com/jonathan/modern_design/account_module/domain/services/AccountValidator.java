package com.jonathan.modern_design.account_module.domain.services;

import com.jonathan.modern_design.account_module.domain.exceptions.AccountIsInactiveException;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.shared.annotations.DomainService;

@DomainService
public class AccountValidator {

    public void validateAccount(Account account) {
        validateActive(account);
    }

    private void validateActive(Account account) {
        if (!account.isActive()) {
            throw new AccountIsInactiveException(account.getAccountNumber());
        }
    }
}

