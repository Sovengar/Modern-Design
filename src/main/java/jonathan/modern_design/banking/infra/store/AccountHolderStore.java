package jonathan.modern_design.banking.infra.store;

import jonathan.modern_design._shared.tags.DataAdapter;
import jonathan.modern_design.banking.domain.models.AccountHolder;
import jonathan.modern_design.banking.domain.store.AccountHolderRepo;
import jonathan.modern_design.banking.infra.store.spring.AccountHolderRepoSpringDataJPA;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@DataAdapter
@RequiredArgsConstructor
class AccountHolderStore implements AccountHolderRepo {
    private final AccountHolderRepoSpringDataJPA repositoryJPA;

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
