package com.jonathan.modern_design.account_module.application.deposit;

import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.config.annotations.DomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@RequiredArgsConstructor
public class DepositService implements DepositUseCase {
    private final AccountRepository repository;

    @Transactional
    @Override
    public void deposit(final DepositCommand command) {
        var account = repository.findOne(command.accountNumber()).orElseThrow();
        account.add(command.amount(), command.currency());
        repository.update(account);
    }
}
