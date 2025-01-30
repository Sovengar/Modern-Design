package com.jonathan.modern_design.account.application.find_account;

import com.jonathan.modern_design.account.domain.model.Account;
import com.jonathan.modern_design.account.domain.AccountRepository;
import com.jonathan.modern_design.common.UseCase;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@UseCase
@RequiredArgsConstructor
public class FindAccountService implements FindAccountUseCase{
    private final AccountRepository accountRepository;

    @Override
    public Optional<Account> findOne(@NonNull Long id) {
        return accountRepository.findOne(id);
    }

}
