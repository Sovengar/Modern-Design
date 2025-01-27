package com.jonathan.modern_design.account.infraestructure.persistence;

import com.jonathan.modern_design.account.application.find_account.FindAccountUseCase;
import com.jonathan.modern_design.account.application.update_account.UpdateAccountUseCase;
import com.jonathan.modern_design.account.domain.Account;
import com.jonathan.modern_design.account.infraestructure.AccountMapper;

import com.jonathan.modern_design.common.PersistenceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class AccountRepositorySpringAdapter implements AccountRepository {
    private final SpringAccountRepository repository;

    @Override
    public Optional<Account> findOne(Long id) {
        return repository
                .findById(id)
                .map(AccountMapper.INSTANCE::toAccount);

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
    public Account create(Account account) {
        var accountEntity = repository.save(AccountMapper.INSTANCE.toAccountEnity(account));
        return AccountMapper.INSTANCE.toAccount(accountEntity);
    }

    @Override
    public void update(Account account) {
        repository.save(AccountMapper.INSTANCE.toAccountEnity(account));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
