package com.jonathan.modern_design.account.application.find_account;

import com.jonathan.modern_design.account.domain.Account;
import com.jonathan.modern_design.account.infraestructure.persistence.AccountRepository;
import com.jonathan.modern_design.common.UseCase;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@UseCase
@RequiredArgsConstructor
public class FindAccountService implements FindAccountUseCase{
    private final AccountRepository accountRepository;

    @Override
    public Optional<Account> findOne(Long id) {
        return accountRepository.findOne(id);
    }
}
