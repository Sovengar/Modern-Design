package com.jonathan.modern_design.account_module.application.send_money;

import com.jonathan.modern_design.shared.Currency;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SendMoneyCommand(String sourceId, String targetId, BigDecimal amount, Currency currency) {
}
