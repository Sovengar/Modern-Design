package com.jonathan.modern_design.account_module.application;

import com.jonathan.modern_design._shared.Currency;

import java.math.BigDecimal;

public interface DepositUseCase {
    void deposit(DepositCommand command);

    record DepositCommand(String accountNumber, BigDecimal amount, Currency currency) {
    }
}
