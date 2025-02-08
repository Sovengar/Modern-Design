package com.jonathan.modern_design.account_module.application.update_account;

import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.model.Account;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateAccountService implements UpdateAccountUseCase {
    private final AccountRepository repository;

    @Override
    public void update(Account account) {
        repository.update(account);
    }
}
