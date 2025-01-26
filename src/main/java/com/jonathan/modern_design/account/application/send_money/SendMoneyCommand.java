package com.jonathan.modern_design.account.application.send_money;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SendMoneyCommand(Long sourceId, Long targetId, BigDecimal amount) {}
