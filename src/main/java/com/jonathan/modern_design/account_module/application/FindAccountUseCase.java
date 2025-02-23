package com.jonathan.modern_design.account_module.application;

import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.user_module.infra.UserResource;

import java.math.BigDecimal;

public interface FindAccountUseCase {
    AccountResource findOne(final String accountNumber);

    record AccountResource(String accountNumber, BigDecimal amount, String currency, UserResource user) {
        public AccountResource(final Account account) {
            this(account.getAccountNumber().getAccountNumber(), account.getMoney().getAmount(), account.getMoney().getCurrency().getCode(), new UserResource(account.getUser()));
        }
    }
}
