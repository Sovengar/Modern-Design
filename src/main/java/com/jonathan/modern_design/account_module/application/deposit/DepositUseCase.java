package com.jonathan.modern_design.account_module.application.deposit;

import com.jonathan.modern_design.shared.Currency;

import java.math.BigDecimal;

public interface DepositUseCase {
    void deposit(String accountNumber, BigDecimal amount, Currency currency);
}
