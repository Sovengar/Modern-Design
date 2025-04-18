package jonathan.modern_design.account_module.infra;

import jonathan.modern_design._common.annotations.Fake;
import jonathan.modern_design._common.annotations.Query;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.AccountEntity;
import jonathan.modern_design.account_module.domain.AccountRepo;
import jonathan.modern_design.account_module.domain.exceptions.AccountNotFoundException;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

interface AccountSpringRepo extends JpaRepository<AccountEntity, String> {
    Optional<AccountEntity> findByAccountNumber(@NonNull String accountNumber);
}

@Query
@Primary //When Spring finds AccountRepository but creates AccountSpringRepo, it will use AccountRepositorySpringAdapter
@RequiredArgsConstructor
class AccountRepoAdapter implements AccountRepo {
    private final AccountSpringRepo repository;
    private final AccountMapper accountMapper;

    @Override
    public Optional<Account> findOne(final String accountNumber) {
        return findOneEntity(accountNumber).map(AccountEntity::toDomain);
    }

    private Optional<AccountEntity> findOneEntity(@NonNull final String accountNumber) {
        return repository.findByAccountNumber(accountNumber);
    }

    private AccountEntity findOneEntityOrElseThrow(@NonNull final String accountNumber) {
        return findOneEntity(accountNumber).orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    @Override
    public Page<Account> findAll(final Pageable pageable) {
        List<Account> accounts = repository.findAll(pageable)
                .getContent()
                .stream()
                .map(AccountEntity::toDomain)
                .toList();

        return new PageImpl<>(accounts, pageable, accounts.size());
    }

    @Override
    public AccountAccountNumber create(final Account account) {
        var accountEntity = new AccountEntity(account);
        repository.save(accountEntity);
        return account.accountAccountNumber();
    }

    @Override
    public void update(final Account account) {
        var accountEntity = findOneEntityOrElseThrow(account.accountAccountNumber().accountNumber());
        accountMapper.updateEntity(accountEntity, account);
        repository.save(accountEntity);
    }

    @Override
    public void delete(final String accountNumber) {
        repository.deleteById(accountNumber);
    }

    @Override
    public void softDelete(final String accountNumber) {
        this.findOneEntity(accountNumber).ifPresent(account -> {
            account.deleted(true);
            repository.save(account);
        });
    }
}

@Fake //This class is for unit tests, also, don't evaluate his state, pointless, rather evaluate the state of the objects
class AccountInMemoryRepo implements AccountRepo {
    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> findOne(String accountNumber) {
        Account account = accounts.get(accountNumber);
        return Optional.ofNullable(account);
    }

    @Override
    public Page<Account> findAll(Pageable pageable) {
        List<Account> accountsList = new ArrayList<>(accounts.values());
        return new PageImpl<>(accountsList, pageable, accountsList.size());
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






