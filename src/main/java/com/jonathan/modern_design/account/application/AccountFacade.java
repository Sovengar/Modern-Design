package com.jonathan.modern_design.account.application;

import com.jonathan.modern_design.account.application.create_account.AccountDataCommand;
import com.jonathan.modern_design.account.application.create_account.CreateAccountService;
import com.jonathan.modern_design.account.application.create_account.CreateAccountUseCase;
import com.jonathan.modern_design.account.application.find_account.FindAccountUseCase;
import com.jonathan.modern_design.account.application.send_money.SendMoneyCommand;
import com.jonathan.modern_design.account.application.send_money.SendMoneyUseCase;
import com.jonathan.modern_design.account.application.update_account.UpdateAccountUseCase;
import com.jonathan.modern_design.account.domain.model.Account;
import com.jonathan.modern_design.account.domain.AccountRepository;
import com.jonathan.modern_design.common.BeanClass;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@BeanClass
@RequiredArgsConstructor
public class AccountFacade implements SendMoneyUseCase, FindAccountUseCase, UpdateAccountUseCase, CreateAccountUseCase {
    private final AccountRepository accountRepository;
    private final SendMoneyUseCase sendMoneyUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final CreateAccountUseCase createAccountUseCase;

    @Override
    public void sendMoney(@NonNull final SendMoneyCommand command) {
        sendMoneyUseCase.sendMoney(command);
    }

    @Override
    public Optional<Account> findOne(@NonNull final Long accountId) {
        return accountRepository.findOne(accountId);
    }

    @Override
    public void update(@NonNull Account account) {
        updateAccountUseCase.update(account);
    }

    @Override
    public Account createAccount(@NonNull final AccountDataCommand command) {
        return createAccountUseCase.createAccount(command);
    }
}
