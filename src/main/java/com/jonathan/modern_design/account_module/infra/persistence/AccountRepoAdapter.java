package com.jonathan.modern_design.account_module.infra.persistence;

import com.jonathan.modern_design._infra.config.annotations.PersistenceAdapter;
import com.jonathan.modern_design.account_module.domain.AccountRepo;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountNumber;
import com.jonathan.modern_design.account_module.infra.mapper.AccountMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Primary //When Spring finds AccountRepository but creates AccountSpringRepo, it will use AccountRepositorySpringAdapter
@RequiredArgsConstructor
public class AccountRepoAdapter implements AccountRepo {
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
