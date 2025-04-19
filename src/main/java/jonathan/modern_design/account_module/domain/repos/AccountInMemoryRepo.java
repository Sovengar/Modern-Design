package jonathan.modern_design.account_module.domain.repos;

import jonathan.modern_design._common.annotations.Fake;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.AccountEntity;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;
import lombok.NonNull;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

@Fake //This class is for unit tests, also, don't evaluate his state, pointless, rather evaluate the state of the objects
public class AccountInMemoryRepo implements AccountRepo {
    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> findOne(String accountNumber) {
        var account = accounts.get(accountNumber);
        return ofNullable(account);
    }

    @Override
    public Optional<AccountEntity> findOneEntity(final @NonNull String accountNumber) {
        var account = accounts.get(accountNumber);
        return of(new AccountEntity(account));
    }

    @Override
    public AccountAccountNumber create(Account account) {
        accounts.put(account.accountAccountNumber().accountNumber(), account);
        return account.accountAccountNumber();
    }

    @Override
    public void update(Account account) {
        requireNonNull(account);
    }

    @Override
    public void delete(final String accountNumber) {
        accounts.remove(accountNumber);
    }

    @Override
    public void softDelete(final String accountNumber) {
        accounts.remove(accountNumber);
    }
}
