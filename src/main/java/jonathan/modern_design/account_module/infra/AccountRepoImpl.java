package jonathan.modern_design.account_module.infra;

import jonathan.modern_design._common.annotations.DataAdapter;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.AccountEntity;
import jonathan.modern_design.account_module.domain.repos.AccountRepo;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;

import java.util.Optional;

@DataAdapter
@Primary //When Spring finds AccountRepository but creates AccountSpringRepo, it will use AccountRepositorySpringAdapter
@RequiredArgsConstructor
class AccountRepoImpl implements AccountRepo {
    private final AccountRepoSpringDataJPA repository;
    private final AccountMapper accountMapper;

    @Override
    public Optional<Account> findOne(final String accountNumber) {
        return findOneEntity(accountNumber).map(AccountEntity::toDomain);
    }

    @Override
    public Optional<AccountEntity> findOneEntity(@NonNull final String accountNumber) {
        return repository.findByAccountNumber(accountNumber);
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






