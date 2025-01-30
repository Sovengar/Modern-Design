package com.jonathan.modern_design.account.infraestructure.persistence;

import com.jonathan.modern_design.account.domain.AccountRepository;
import com.jonathan.modern_design.account.domain.model.Account;
import com.jonathan.modern_design.account.application.AccountMapper;

import com.jonathan.modern_design.common.PersistenceAdapter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class AccountRepositorySpringAdapter implements AccountRepository {
    private final SpringAccountRepository repository;

    @Override
    public Optional<Account> findOne(@NonNull final Long id) {
        return findOneEntity(id).map(AccountMapper.INSTANCE::toAccount);
    }

    @Override
    public Page<Account> findAll(Pageable pageable) {
        List<Account> accounts = repository.findAll(pageable)
                .getContent()
                .stream()
                .map(AccountMapper.INSTANCE::toAccount)
        .toList();

        return new PageImpl<>(accounts, pageable, accounts.size());
    }

    @Override
    public Account create(@NonNull Account account) {
        final var accountEntity = repository.save(AccountMapper.INSTANCE.toAccountEnity(account));
        return AccountMapper.INSTANCE.toAccount(accountEntity);
    }

    @Override
    public void update(@NonNull Account account) {
        repository.save(AccountMapper.INSTANCE.toAccountEnity(account));
    }

    @Override
    public void delete(@NonNull final Long id) {
        repository.deleteById(id);
    }

    @Override
    public void softDelete(@NonNull final Long id) {
        this.findOneEntity(id).ifPresent(account -> {
            account.setDeleted(true);
            repository.save(account);
        });
    }

    private Optional<AccountEntity> findOneEntity(@NonNull final Long id) {
        return repository.findById(id);
    }

}
