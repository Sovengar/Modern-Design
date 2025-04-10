package jonathan.modern_design.account_module.dtos;

import jonathan.modern_design._shared.Currency;

import java.math.BigDecimal;

public record TransferMoneyCommand(String sourceId, String targetId, BigDecimal amount, Currency currency) {
}
