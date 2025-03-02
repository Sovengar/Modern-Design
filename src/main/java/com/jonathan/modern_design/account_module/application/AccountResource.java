package com.jonathan.modern_design.account_module.application;

import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.user_module.application.UserResource;

import java.math.BigDecimal;

public record AccountResource(String accountNumber, BigDecimal amount, String currency, UserResource user) {
    public AccountResource(final Account account) {
        this(account.getAccountNumber().getValue(), account.getMoney().getAmount(), account.getMoney().getCurrency().getCode(), UserResource.from(account.getUser()));
    }
}
