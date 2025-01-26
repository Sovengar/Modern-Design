package com.jonathan.modern_design.account.infraestructure.persistence;

import com.jonathan.modern_design.account.application.find_account.FindAccountUseCase;
import com.jonathan.modern_design.account.application.update_account.UpdateAccountUseCase;
import com.jonathan.modern_design.account.domain.Account;
import com.jonathan.modern_design.account.domain.exceptions.AccountNotFoundException;
import com.jonathan.modern_design.account.infraestructure.AccountMapper;

import com.jonathan.modern_design.common.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class AccountRepositorySpringAdapter implements FindAccountUseCase, UpdateAccountUseCase {
    private final SpringAccountRepository accountRepository;

    @Override
    public Optional<Account> find(Long id) {
        return accountRepository
                .findById(id)
                .map(AccountMapper.INSTANCE::toAccount);
                //.orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Override
    public void update(Account account) {
        accountRepository.save(AccountMapper.INSTANCE.toAccountEnity(account));
    }
}
