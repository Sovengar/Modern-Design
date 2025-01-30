package com.jonathan.modern_design.account.application.send_money;

import com.jonathan.modern_design.common.Currency;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SendMoneyCommand(Long sourceId, Long targetId, BigDecimal amount, Currency currency) {}
