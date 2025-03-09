package com.jonathan.modern_design.account_module.dtos;

import com.jonathan.modern_design._shared.Currency;

import java.math.BigDecimal;

public record WithdrawMoneyCommand(String accountNumber, BigDecimal amount, Currency currency) {
}
