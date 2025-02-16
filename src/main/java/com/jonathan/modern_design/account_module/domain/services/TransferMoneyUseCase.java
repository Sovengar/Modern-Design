package com.jonathan.modern_design.account_module.domain.services;

import com.jonathan.modern_design._shared.Currency;
import lombok.NonNull;

import java.math.BigDecimal;

public interface TransferMoneyUseCase {
    void transferMoney(@NonNull final TransferMoneyCommand command);

    record TransferMoneyCommand(String sourceId, String targetId, BigDecimal amount, Currency currency) {
    }
}
