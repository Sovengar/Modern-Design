package com.jonathan.modern_design.account_module.application.deposit;

import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.config.annotations.DomainService;
import com.jonathan.modern_design.shared.Currency;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@DomainService
@RequiredArgsConstructor
public class DepositService implements DepositUseCase {
    private final AccountRepository repository;

    @Override
    public void deposit(String accountNumber, BigDecimal amount, Currency currency) {
        repository.deposit(accountNumber, amount, currency);
    }
}
