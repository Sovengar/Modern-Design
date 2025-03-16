package jonathan.modern_design.account_module.domain;

import jonathan.modern_design.account_module.domain.exceptions.AccountNotFoundException;
import jonathan.modern_design.account_module.domain.vo.AccountNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AccountRepo {
    //Commands
    AccountNumber create(Account account);

    void update(Account account);

    void delete(final String accountNumber);

    void softDelete(final String accountNumber);

    //Queries
    Optional<Account> findOne(final String accountNumber);

    Page<Account> findAll(final Pageable pageable);

    default Account findOneOrElseThrow(final String accountNumber) {
        return findOne(accountNumber).orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }
}
