package com.jonathan.modern_design.account.application.send_money;

import com.jonathan.modern_design.common.Currency;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record SendMoneyCommand(UUID sourceId, UUID targetId, BigDecimal amount, Currency currency) {}
