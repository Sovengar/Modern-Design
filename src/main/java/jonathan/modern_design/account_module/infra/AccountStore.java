package jonathan.modern_design.account_module.infra;

import jonathan.modern_design._common.annotations.DataAdapter;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.AccountEntity;
import jonathan.modern_design.account_module.domain.AccountJdbcEntity;
import jonathan.modern_design.account_module.domain.exceptions.AccountNotFoundException;
import jonathan.modern_design.account_module.domain.repos.AccountRepo;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

interface AccountRepoSpringDataJDBC extends CrudRepository<AccountJdbcEntity, String> {
    Optional<AccountJdbcEntity> findByAccountNumber(@NonNull String accountNumber);
}

interface AccountRepoSpringDataJPA extends JpaRepository<AccountEntity, String> {
    Optional<AccountEntity> findByAccountNumber(@NonNull String accountNumber);
}

@DataAdapter
@Primary //When Spring finds AccountRepository but creates AccountSpringRepo, it will use AccountRepositorySpringAdapter
@RequiredArgsConstructor
class AccountStore implements AccountRepo {
    private final AccountRepoSpringDataJPA repositoryJPA;
    private final AccountRepoSpringDataJDBC repositoryJDBC;

    @Override
    public Optional<Account> findOne(final String accountNumber) {
        return findOneEntity(accountNumber).map(AccountEntity::toDomain);
        // Use JDBC return accountEntity.map(AccountJdbcEntity::toDomain);
    }

    @Override
    public AccountAccountNumber create(final Account account) {
        var accountEntity = new AccountEntity(account);
        repositoryJPA.save(accountEntity);
        return account.accountAccountNumber();
    }

    /// BEGIN UPDATE /////////
    @Override
    public void update(final Account account) {
        var accountEntity = findOneEntityOrElseThrow(account.accountAccountNumber().accountNumber());
        AccountDataMapper.updateEntity(accountEntity, account);
        repositoryJPA.save(accountEntity);
    }

    private void updateWithJdbc(final Account account) {
        var accountEntity = repositoryJDBC.findByAccountNumber(account.accountAccountNumber().accountNumber()).orElseThrow();
        accountEntity = new AccountJdbcEntity(accountEntity, account);
        repositoryJDBC.save(accountEntity);
    }

    /// /////////

    @Override
    public void delete(final String accountNumber) {
        repositoryJPA.deleteById(accountNumber);
    }

    @Override
    public void softDelete(final String accountNumber) {
        this.findOneEntity(accountNumber).ifPresent(account -> {
            account.deleted(true);
            repositoryJPA.save(account);
        });
    }

    private Optional<AccountEntity> findOneEntity(@NonNull final String accountNumber) {
        return repositoryJPA.findByAccountNumber(accountNumber);
        // JDBC return repositoryJDBC.findByAccountNumber(accountNumber);
    }

    private AccountEntity findOneEntityOrElseThrow(@NonNull final String accountNumber) {
        return findOneEntity(accountNumber).orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }
}

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class AccountDataMapper {
    public static void updateEntity(AccountEntity accountEntity, Account account) {
        accountEntity.accountNumber(account.accountAccountNumber().accountNumber());
        accountEntity.balance(account.money().amount());
        accountEntity.currency(account.money().currency());
        accountEntity.address(account.address().toString());
        accountEntity.userId(account.userId());
        accountEntity.dateOfLastTransaction(account.dateOfLastTransaction());
        accountEntity.active(account.active());
    }
}
