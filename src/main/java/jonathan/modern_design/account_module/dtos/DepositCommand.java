package jonathan.modern_design.account_module.dtos;

import jonathan.modern_design._shared.Currency;

import java.math.BigDecimal;

public record DepositCommand(String accountNumber, BigDecimal amount, Currency currency) {
}
