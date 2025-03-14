package com.jonathan.modern_design.account_module.infra;

import com.jonathan.modern_design._infra.config.annotations.Fake;
import com.jonathan.modern_design._infra.config.annotations.PersistenceAdapter;
import com.jonathan.modern_design.account_module.domain.AccountRepo;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountNumber;
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

@PersistenceAdapter
@Primary //When Spring finds AccountRepository but creates AccountSpringRepo, it will use AccountRepositorySpringAdapter
@RequiredArgsConstructor
class AccountRepoAdapter implements AccountRepo {
    private final AccountSpringRepo repository;
    private final AccountMapper accountMapper;

    @Override
    public Optional<Account> findOne(final String accountNumber) {
        return findOneEntity(accountNumber).map(accountMapper::toAccount);
    }

    private Optional<AccountEntity> findOneEntity(@NonNull final String accountNumber) {
        return repository.findByAccountNumber(accountNumber);
    }

    @Override
    public Page<Account> findAll(final Pageable pageable) {
        List<Account> accounts = repository.findAll(pageable)
                .getContent()
                .stream()
                .map(accountMapper::toAccount)
                .toList();

        return new PageImpl<>(accounts, pageable, accounts.size());
    }

    @Override
    public AccountNumber create(final Account account) {
        var accountEntity = accountMapper.toAccountEntity(account);
        repository.save(accountEntity);
        return account.getAccountNumber();
    }

    @Override
    public void update(final Account account) {
        var accountEntity = findOneEntity(account.getAccountNumber().getValue()).orElseThrow();
        accountMapper.updateAccountEntity(account, accountEntity);
        repository.save(accountEntity);
    }

    @Override
    public void delete(final String accountNumber) {
        repository.deleteById(accountNumber);
    }

    @Override
    public void softDelete(final String accountNumber) {
        this.findOneEntity(accountNumber).ifPresent(account -> {
            account.setDeleted(true);
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
    public AccountNumber create(Account account) {
        accounts.put(account.getAccountNumber().getValue(), account);
        return account.getAccountNumber();
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
