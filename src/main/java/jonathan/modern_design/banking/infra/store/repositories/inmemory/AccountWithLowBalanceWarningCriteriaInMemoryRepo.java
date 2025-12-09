package jonathan.modern_design.banking.infra.store.repositories.inmemory;

import jonathan.modern_design._shared.domain.catalogs.Currency;
import jonathan.modern_design._shared.domain.models.Country;
import jonathan.modern_design._shared.domain.vo.Money;
import jonathan.modern_design._shared.tags.adapters.DataAdapter;
import jonathan.modern_design._shared.tags.persistence.InMemoryOnlyCatalog;
import jonathan.modern_design.banking.domain.models.AccountWithLowBalanceWarningCriteria;
import jonathan.modern_design.banking.domain.store.AccountWithLowBalanceWarningCriteriaRepo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataAdapter
@InMemoryOnlyCatalog
class AccountWithLowBalanceWarningCriteriaInMemoryRepo implements AccountWithLowBalanceWarningCriteriaRepo {
    private final List<AccountWithLowBalanceWarningCriteria> criterias = new ArrayList<>(
            List.of(
                    new AccountWithLowBalanceWarningCriteria(new Country("ES", "Spain"), Money.of(BigDecimal.valueOf(1), Currency.EUR), 100),
                    new AccountWithLowBalanceWarningCriteria(new Country("FR", "France"), Money.of(BigDecimal.valueOf(100), Currency.EUR), 50)
            )
    );

    @Override
    public Optional<AccountWithLowBalanceWarningCriteria> findByCountry(final Country country) {
        return criterias.stream().filter(c -> c.country().equals(country)).findFirst();
    }
}
