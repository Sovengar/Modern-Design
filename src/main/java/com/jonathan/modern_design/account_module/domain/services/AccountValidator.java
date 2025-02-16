package com.jonathan.modern_design.account_module.domain.services;

import com.jonathan.modern_design._infra.config.annotations.DomainService;
import com.jonathan.modern_design.account_module.domain.exceptions.AccountIsInactiveException;
import com.jonathan.modern_design.account_module.domain.model.Account;

@DomainService
public class AccountValidator {

    public void validateAccount(Account account) {
        validateActive(account);
    }

    private void validateActive(Account account) {
        if (!account.isActive()) {
            throw new AccountIsInactiveException(account.getAccountNumber().getAccountNumber());
        }
    }
}

