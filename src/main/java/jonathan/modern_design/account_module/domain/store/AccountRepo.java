package jonathan.modern_design.account_module.domain.store;

import jonathan.modern_design.account_module.domain.exceptions.AccountNotFoundException;
import jonathan.modern_design.account_module.domain.models.account.Account;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAccountNumber;

import java.util.Optional;

public interface AccountRepo {
    AccountAccountNumber create(Account account);

    void update(Account account);

    void delete(final String accountNumber);

    Optional<Account> findById(final Account.Id id);

    Optional<Account> findByAccNumber(final String accountNumber);

    default Account findByAccNumberOrElseThrow(final String accountNumber) {
        return findByAccNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }
}
