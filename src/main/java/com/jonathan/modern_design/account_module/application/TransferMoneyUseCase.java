package com.jonathan.modern_design.account_module.application;

import com.jonathan.modern_design._shared.Currency;

import java.math.BigDecimal;

public interface TransferMoneyUseCase {
    void transferMoney(final TransferMoneyCommand command);

    record TransferMoneyCommand(String sourceId, String targetId, BigDecimal amount, Currency currency) {
    }
}
