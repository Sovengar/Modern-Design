package jonathan.modern_design.banking.domain.models;

import jonathan.modern_design._shared.domain.models.Country;
import jonathan.modern_design._shared.domain.vo.Money;

public record AccountWithLowBalanceWarningCriteria(Country country, Money balanceTrigger, int daysWithThatBalance) {
}
