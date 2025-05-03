package jonathan.modern_design.account_module.domain.store;

import jonathan.modern_design._common.annotations.Fake;
import jonathan.modern_design.account_module.domain.models.account.Account;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAccountNumber;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

@Fake //This class is for unit tests, also, don't evaluate his state, pointless, rather evaluate the state of the objects
public class AccountRepoInMemory implements AccountRepo {
    private final ConcurrentHashMap<String, Account> accountsByNumber = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Account.Id, Account> accountsById = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> findByAccNumber(String accountNumber) {
        var account = accountsByNumber.get(accountNumber);
        return ofNullable(account);
    }

    @Override
    public AccountAccountNumber create(Account account) {
        accountsByNumber.put(account.getAccountAccountNumber().getAccountNumber(), account);
        accountsById.put(account.getAccountId(), account);
        return account.getAccountAccountNumber();
    }

    @Override
    public void update(Account account) {
        requireNonNull(account);
        accountsByNumber.put(account.getAccountAccountNumber().getAccountNumber(), account);
        accountsById.put(account.getAccountId(), account);
    }

    @Override
    public void delete(final String accountNumber) {
        var account = accountsByNumber.get(accountNumber);
        if (account != null) {
            accountsById.remove(account.getAccountId());
            accountsByNumber.remove(accountNumber);
        }
    }

    @Override
    public Optional<Account> findById(final Account.Id id) {
        return ofNullable(accountsById.get(id));
    }
}
