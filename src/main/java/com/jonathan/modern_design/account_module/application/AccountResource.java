package com.jonathan.modern_design.account_module.application;

import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.user_module.domain.model.User;

import java.math.BigDecimal;

public record AccountResource(String accountNumber, BigDecimal amount, String currency, User.ID userId) {
    public AccountResource(final Account account) {
        this(account.getAccountNumber().getValue(), account.getMoney().getAmount(), account.getMoney().getCurrency().getCode(), account.getUserId());
    }
}
