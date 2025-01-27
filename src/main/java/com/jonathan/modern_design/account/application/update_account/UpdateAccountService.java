package com.jonathan.modern_design.account.application.update_account;

import com.jonathan.modern_design.account.domain.Account;
import com.jonathan.modern_design.account.infraestructure.persistence.AccountRepository;
import com.jonathan.modern_design.common.UseCase;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UpdateAccountService implements UpdateAccountUseCase {
    private final AccountRepository accountRepository;

    @Override
    public void update(Account account) {
        accountRepository.update(account);
    }
}
