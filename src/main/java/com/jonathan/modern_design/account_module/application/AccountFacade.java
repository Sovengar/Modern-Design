package com.jonathan.modern_design.account_module.application;

import com.jonathan.modern_design.account_module.application.create_account.CreateAccountCommand;
import com.jonathan.modern_design.account_module.application.create_account.CreateAccountUseCase;
import com.jonathan.modern_design.account_module.application.find_account.FindAccountUseCase;
import com.jonathan.modern_design.account_module.application.transfer_money.TransferMoneyCommand;
import com.jonathan.modern_design.account_module.application.transfer_money.TransferMoneyUseCase;
import com.jonathan.modern_design.account_module.application.update_account.UpdateAccountUseCase;
import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.config.annotations.BeanClass;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@BeanClass
@RequiredArgsConstructor
public class AccountFacade implements TransferMoneyUseCase, FindAccountUseCase, UpdateAccountUseCase, CreateAccountUseCase {
    private final AccountRepository accountRepository;
    private final TransferMoneyUseCase transferMoneyUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final CreateAccountUseCase createAccountUseCase;

    @Override
    public void transferMoney(@NonNull final TransferMoneyCommand command) {
        transferMoneyUseCase.transferMoney(command);
    }

    @Override
    public Optional<Account> findOne(@NonNull final String accountNumber) {
        return accountRepository.findOne(accountNumber);
    }

    @Override
    public void update(@NonNull Account account) {
        updateAccountUseCase.update(account);
    }

    @Override
    public Account createAccount(@NonNull final CreateAccountCommand command) {
        return createAccountUseCase.createAccount(command);
    }
}
