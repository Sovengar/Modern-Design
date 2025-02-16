package com.jonathan.modern_design.account_module.domain.services;

import com.jonathan.modern_design._shared.Currency;

import java.math.BigDecimal;

public interface WithdrawMoneyUseCase {
    void withdrawMoney(final WithdrawMoneyCommand command);

    record WithdrawMoneyCommand(String accountNumber, BigDecimal amount, Currency currency) {
    }
}
