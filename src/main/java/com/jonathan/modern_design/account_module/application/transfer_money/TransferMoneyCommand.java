package com.jonathan.modern_design.account_module.application.transfer_money;

import com.jonathan.modern_design._shared.Currency;

import java.math.BigDecimal;

public record TransferMoneyCommand(String sourceId, String targetId, BigDecimal amount, Currency currency) {
}
