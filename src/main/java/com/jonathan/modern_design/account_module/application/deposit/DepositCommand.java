package com.jonathan.modern_design.account_module.application.deposit;

import com.jonathan.modern_design.shared.Currency;

import java.math.BigDecimal;

public record DepositCommand(String accountNumber, BigDecimal amount, Currency currency) {
}
