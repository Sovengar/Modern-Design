package jonathan.modern_design.banking.domain.events;

import jonathan.modern_design._shared.vo.Money;

public record MoneyWithdrawed(Money money, String accountNumber) {
}
