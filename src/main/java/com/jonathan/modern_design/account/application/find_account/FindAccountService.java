package com.jonathan.modern_design.account.application.find_account;

import com.jonathan.modern_design.account.domain.model.Account;
import com.jonathan.modern_design.account.domain.AccountRepository;
import com.jonathan.modern_design.common.UseCase;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class FindAccountService implements FindAccountUseCase{
    private final AccountRepository accountRepository;

    @Override
    public Optional<Account> findOne(@NonNull UUID id) {
        return accountRepository.findOne(id);
    }

}
