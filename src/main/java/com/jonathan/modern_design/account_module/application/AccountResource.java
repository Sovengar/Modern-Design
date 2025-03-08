package com.jonathan.modern_design.account_module.application;

import com.jonathan.modern_design.account_module.domain.model.Account;

import java.math.BigDecimal;

public record AccountResource(String accountNumber, BigDecimal amount, String currency, Long userId) {
    public AccountResource(final Account account) {
        this(account.getAccountNumber().getValue(), account.getMoney().getAmount(), account.getMoney().getCurrency().getCode(), account.getUserId());
    }
}
