package com.jonathan.modern_design.account_module.application;

import com.jonathan.modern_design._infra.config.annotations.BeanClass;
import com.jonathan.modern_design.account_module.application.find_account.FindAccountUseCase;
import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.services.CreateAccountUseCase;
import com.jonathan.modern_design.account_module.domain.services.DepositUseCase;
import com.jonathan.modern_design.account_module.domain.services.TransferMoneyUseCase;
import com.jonathan.modern_design.account_module.domain.services.UpdateAccountUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@BeanClass
@RequiredArgsConstructor
@Transactional
public class AccountFacade implements TransferMoneyUseCase, FindAccountUseCase, UpdateAccountUseCase, CreateAccountUseCase, DepositUseCase {
    private final AccountRepository accountRepository;
    private final TransferMoneyUseCase transferMoneyUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final CreateAccountUseCase createAccountUseCase;
    private final DepositUseCase depositUseCase;

    @Override
    public void transferMoney(final TransferMoneyCommand command) {
        transferMoneyUseCase.transferMoney(command);
    }

    @Override
    public Optional<Account> findOne(final String accountNumber) {
        return accountRepository.findOne(accountNumber);
    }

    @Override
    public void update(Account account) {
        updateAccountUseCase.update(account);
    }

    @Override
    public Account createAccount(final CreateAccountCommand command) {
        return createAccountUseCase.createAccount(command);
    }

    @Override
    public Account deposit(final DepositCommand command) {
        return depositUseCase.deposit(command);
    }
}
