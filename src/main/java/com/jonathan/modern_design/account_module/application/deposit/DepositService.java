package com.jonathan.modern_design.account_module.application.deposit;

import com.jonathan.modern_design._infra.config.annotations.DomainService;
import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@RequiredArgsConstructor
public class DepositService implements DepositUseCase {
    private final AccountRepository repository;

    @Transactional
    @Override
    public Account deposit(final DepositCommand command) {
        var account = repository.findOne(command.accountNumber()).orElseThrow();
        account.add(command.amount(), command.currency());
        repository.update(account);

        return repository.findOne(command.accountNumber()).orElseThrow();
    }
}
