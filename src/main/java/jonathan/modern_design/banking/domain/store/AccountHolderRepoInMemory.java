package jonathan.modern_design.banking.domain.store;

import jonathan.modern_design._shared.tags.Fake;
import jonathan.modern_design.banking.domain.models.AccountHolder;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

@Fake
public class AccountHolderRepoInMemory implements AccountHolderRepo {
    private final ConcurrentHashMap<UUID, AccountHolder> accountsById = new ConcurrentHashMap<>();

    @Override
    public Optional<AccountHolder> findById(final UUID id) {
        return ofNullable(accountsById.get(id));
    }

    @Override
    public UUID save(final AccountHolder accountHolder) {
        accountsById.put(accountHolder.getId(), accountHolder);
        return accountHolder.getId();
    }
}
