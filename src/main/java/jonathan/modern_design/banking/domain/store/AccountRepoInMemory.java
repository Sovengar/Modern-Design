package jonathan.modern_design.banking.domain.store;

import jonathan.modern_design._shared.tags.Fake;
import jonathan.modern_design.banking.domain.models.Account;
import jonathan.modern_design.banking.domain.vo.AccountNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

@Fake
public class AccountRepoInMemory implements AccountRepo {
    private final ConcurrentHashMap<String, Account> accountsByNumber = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Account.Id, Account> accountsById = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> findByAccNumber(String accountNumber) {
        var account = accountsByNumber.get(accountNumber);
        return ofNullable(account);
    }

    @Override
    public List<Account> findAll() {
        var accounts = new ArrayList<Account>();
        accountsByNumber.forEach((key, value) -> accounts.add(value));
        return accounts;
    }

    @Override
    public AccountNumber create(Account account) {
        accountsByNumber.put(account.getAccountNumber().getAccountNumber(), account);
        return account.getAccountNumber();
    }

    @Override
    public void update(Account account) {
        requireNonNull(account);
        accountsByNumber.put(account.getAccountNumber().getAccountNumber(), account);
    }

    @Override
    public void delete(final String accountNumber) {
        var account = accountsByNumber.get(accountNumber);
        if (account != null) {
            accountsByNumber.remove(accountNumber);
        }
    }
}
