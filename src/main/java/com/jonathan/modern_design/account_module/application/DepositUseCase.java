package com.jonathan.modern_design.account_module.application;

import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.account_module.domain.model.Account;

import java.math.BigDecimal;

public interface DepositUseCase {
    Account deposit(DepositCommand command);

    record DepositCommand(String accountNumber, BigDecimal amount, Currency currency) {
    }
}
