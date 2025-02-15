package com.jonathan.modern_design.account_module.application.send_money;

import com.jonathan.modern_design.shared.Currency;

import java.math.BigDecimal;

public record SendMoneyCommand(String sourceId, String targetId, BigDecimal amount, Currency currency) {
}
