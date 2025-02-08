package com.jonathan.modern_design.account_module.application.deposit;

import com.jonathan.modern_design.shared.Currency;

import java.math.BigDecimal;
import java.util.UUID;

public interface DepositUseCase {
    void deposit(UUID id, BigDecimal amount, Currency currency);
}
