package jonathan.modern_design.banking.infra.store.repositories.postgres;

import jonathan.modern_design._shared.tags.adapters.DataAdapter;
import jonathan.modern_design.banking.domain.models.AccountHolder;
import jonathan.modern_design.banking.domain.store.AccountHolderRepo;
import jonathan.modern_design.banking.infra.store.repositories.spring_jpa.AccountHolderSpringJpaRepo;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@DataAdapter
@RequiredArgsConstructor
class AccountHolderPostgreRepo implements AccountHolderRepo {
    private final AccountHolderSpringJpaRepo repositoryJPA;

    @Override
    public Optional<AccountHolder> findById(UUID id) {
        return repositoryJPA.findById(id);
    }

    @Override
    public UUID save(final AccountHolder accountHolder) {
        var accHolder = repositoryJPA.save(accountHolder);
        return accHolder.getId();
    }
}
