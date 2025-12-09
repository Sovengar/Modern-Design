package jonathan.modern_design.banking.domain.events;

import jonathan.modern_design._shared.domain.vo.Money;


public record MoneyDeposited(
        Money money,
        //Exposing a VO, locally, no problem, but even if it was externalized, there would be no problem, very stable, unlikely to change
        String accountNumber
) {
}
