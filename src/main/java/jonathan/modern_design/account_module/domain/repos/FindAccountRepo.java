package jonathan.modern_design.account_module.domain.repos;

import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.AccountEntity;
import jonathan.modern_design.account_module.domain.exceptions.AccountNotFoundException;
import lombok.NonNull;

import java.util.Optional;

public interface FindAccountRepo {
    Optional<Account> findOne(final String accountNumber);

    default Account findOneOrElseThrow(final String accountNumber) {
        return findOne(accountNumber).orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    Optional<AccountEntity> findOneEntity(@NonNull final String accountNumber);

    default AccountEntity findOneEntityOrElseThrow(@NonNull final String accountNumber) {
        return findOneEntity(accountNumber).orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }
}
