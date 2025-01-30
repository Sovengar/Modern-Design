package com.jonathan.modern_design.account.application.update_account;

import com.jonathan.modern_design.account.domain.model.Account;
import com.jonathan.modern_design.account.domain.AccountRepository;
import com.jonathan.modern_design.common.UseCase;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UpdateAccountService implements UpdateAccountUseCase {
    private final AccountRepository repository;

    @Override
    public void update(Account account) {
        repository.update(account);
    }
}
