package jonathan.modern_design.banking.domain.store;

import jonathan.modern_design.banking.domain.models.AccountHolder;

import java.util.Optional;
import java.util.UUID;

public interface AccountHolderRepo {
    Optional<AccountHolder> findById(UUID id);

    default AccountHolder findByIdOrElseThrow(UUID id) {
        return findById(id).orElseThrow();
    }

    UUID save(AccountHolder accountHolder);
}
