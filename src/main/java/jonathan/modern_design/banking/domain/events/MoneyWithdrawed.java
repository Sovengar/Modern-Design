package jonathan.modern_design.banking.domain.events;

import jonathan.modern_design._shared.domain.vo.Money;

public record MoneyWithdrawed(Money money, String accountNumber) {
}
