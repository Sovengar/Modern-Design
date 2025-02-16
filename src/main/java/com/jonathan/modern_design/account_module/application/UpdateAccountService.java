package com.jonathan.modern_design.account_module.application;

import com.jonathan.modern_design._infra.config.annotations.DomainService;
import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.services.UpdateAccountUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@RequiredArgsConstructor
public class UpdateAccountService implements UpdateAccountUseCase {
    private final AccountRepository repository;

    @Transactional
    @Override
    public void update(Account account) {
        repository.update(account);
    }
}
