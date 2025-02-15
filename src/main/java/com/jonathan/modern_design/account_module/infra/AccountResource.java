package com.jonathan.modern_design.account_module.infra;

import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.user_module.dtos.UserResource;

import java.math.BigDecimal;

public record AccountResource(String accountNumber, BigDecimal amount, String currency, UserResource user) {
    public AccountResource(final Account account) {
        this(account.getAccountNumber(), account.getMoney().getAmount(), account.getMoney().getCurrency().getCode(), new UserResource(account.getUser()));
    }
}
