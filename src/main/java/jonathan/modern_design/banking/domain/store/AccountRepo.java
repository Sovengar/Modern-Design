package jonathan.modern_design.banking.domain.store;

import jonathan.modern_design.banking.domain.exceptions.AccountNotFoundException;
import jonathan.modern_design.banking.domain.models.Account;
import jonathan.modern_design.banking.domain.vo.AccountNumber;

import java.util.List;
import java.util.Optional;

public interface AccountRepo {
    AccountNumber create(Account account);

    void update(Account account);

    void delete(final String accountNumber);

    Optional<Account> findByAccNumber(final String accountNumber);

    default Account findByAccNumberOrElseThrow(final String accountNumber) {
        return findByAccNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    List<Account> findAll();
}
