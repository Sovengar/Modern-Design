package jonathan.modern_design.banking.infra.store;

import jonathan.modern_design._shared.tags.DataAdapter;
import jonathan.modern_design.banking.domain.exceptions.AccountNotFoundException;
import jonathan.modern_design.banking.domain.models.Account;
import jonathan.modern_design.banking.domain.models.AccountEntity;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import jonathan.modern_design.banking.domain.vo.AccountNumber;
import jonathan.modern_design.banking.infra.store.spring.AccountRepoSpringDataJPA;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@DataAdapter
@Primary //When Spring finds AccountRepository but creates AccountSpringRepo, it will use AccountRepositorySpringAdapter
@RequiredArgsConstructor
class AccountStore implements AccountRepo {
    private final AccountRepoSpringDataJPA repositoryJPA;

    @Override
    public Optional<Account> findByAccNumber(final String accountNumber) {
        var accountEntity = findOneEntity(accountNumber);
        return accountEntity.map(Account::new);
    }

    @Override
    public AccountNumber create(final Account account) {
        var accountEntity = new AccountEntity(account);
        repositoryJPA.save(accountEntity);
        return account.getAccountNumber();
    }

    @Override
    public void update(final Account account) {
        var accountEntity = findOneEntityOrElseThrow(account.getAccountNumber().getAccountNumber());
        AccountDataMapper.mapFromDomainToDataModel(accountEntity, account);
        repositoryJPA.save(accountEntity);
    }

    @Override
    public void delete(final String accountNumber) {
        var accountEntity = repositoryJPA.findByAccountNumber(accountNumber).orElseThrow();
        repositoryJPA.deleteById(accountEntity.getId());
    }

    @Override
    public Optional<Account> findById(final Account.Id id) {
        return repositoryJPA.findById(id.id()).map(Account::new);
    }

    @Transactional(readOnly = true) //When using Streams, a transaction is needed to keep the session open
    public void processAllActiveAccounts() {
        try (Stream<AccountEntity> stream = repositoryJPA.streamAllActiveAccounts()) {
            stream.forEach(account -> {
                // Process Account
            });
        }
    }

    @Override
    public List<Account> findAll() {
        return repositoryJPA.findAll().stream().map(Account::new).toList();
    }

    private Optional<AccountEntity> findOneEntity(@NonNull final String accountNumber) {
        return repositoryJPA.findByAccountNumber(accountNumber);
    }

    private AccountEntity findOneEntityOrElseThrow(@NonNull final String accountNumber) {
        return findOneEntity(accountNumber).orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }
}

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class AccountDataMapper {
    public static void mapFromDomainToDataModel(AccountEntity accountEntity, Account account) {
        accountEntity.updateFrom(account);
    }
}
