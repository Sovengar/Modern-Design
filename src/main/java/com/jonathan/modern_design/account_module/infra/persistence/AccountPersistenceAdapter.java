package com.jonathan.modern_design.account_module.infra.persistence;

import com.jonathan.modern_design._infra.config.annotations.PersistenceAdapter;
import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.model.Account;
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
@Primary //When Spring finds AccountRepository and AccountRepositorySpringAdapter, it will use AccountRepositorySpringAdapter
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements AccountRepository { //TODO FIX, AQUI DEBERIA LLAMAR LAS INTERFACES, YA QUE ALOMEJOR LA ESCRITURA ES DB Y LA LECTURA ES DE UN JSON
    private final SpringAccountRepository repository;
    private final AccountMapper accountMapper;

    @Override
    public Optional<Account> findOne(final String accountNumber) {
        return findOneEntity(accountNumber).map(accountMapper::toAccount);
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
    public Account create(final Account account) {
        var accountEntity = accountMapper.toAccountEntity(account);
        accountEntity = repository.save(accountEntity);
        //TODO FIX DETACHED USER

        return accountMapper.toAccount(accountEntity);
    }

    @Override
    public void update(final Account account) {
        var accountEntity = findOneEntity(account.getAccountNumber().getAccountNumber()).orElseThrow();
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

    private Optional<AccountEntity> findOneEntity(@NonNull final String accountNumber) {
        return repository.findByAccountNumber(accountNumber);
    }

}
