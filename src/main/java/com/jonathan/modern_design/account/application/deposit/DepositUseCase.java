package com.jonathan.modern_design.account.application.deposit;

import com.jonathan.modern_design.common.Currency;

import java.math.BigDecimal;
import java.util.UUID;

public interface DepositUseCase {
    void deposit(UUID id, BigDecimal amount, Currency currency);
}
