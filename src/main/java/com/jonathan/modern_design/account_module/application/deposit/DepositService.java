package com.jonathan.modern_design.account_module.application.deposit;

import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.common.Currency;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
public class DepositService implements DepositUseCase {
    private final AccountRepository repository;

    @Override
    public void deposit(UUID accountId, BigDecimal amount, Currency currency) {
        repository.deposit(accountId, amount, currency);
    }
}
