package com.jonathan.modern_design.account_module.application.find_account;

import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.shared.annotations.DomainService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class FindAccountService implements FindAccountUseCase {
    private final AccountRepository accountRepository;

    @Override
    public Optional<Account> findOne(@NonNull UUID id) {
        return accountRepository.findOne(id);
    }

}
