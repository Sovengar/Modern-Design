package com.jonathan.modern_design.account.application.deposit;

import com.jonathan.modern_design.account.domain.AccountRepository;
import com.jonathan.modern_design.common.Currency;
import com.jonathan.modern_design.common.UseCase;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class DepositService implements DepositUseCase {
    private final AccountRepository repository;

    @Override
    public void deposit(UUID accountId, BigDecimal amount, Currency currency) {
        repository.deposit(accountId, amount, currency);
    }
}
