package jonathan.modern_design.banking.domain.store;

import jonathan.modern_design._shared.domain.models.Country;
import jonathan.modern_design.banking.domain.models.AccountWithLowBalanceWarningCriteria;

import java.util.Optional;

public interface AccountWithLowBalanceWarningCriteriaRepo {
    //Not all countries have a criteria
    Optional<AccountWithLowBalanceWarningCriteria> findByCountry(Country country);
}
