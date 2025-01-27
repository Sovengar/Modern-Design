package com.jonathan.modern_design.account.application;

import com.jonathan.modern_design.account.application.find_account.FindAccountUseCase;
import com.jonathan.modern_design.account.application.send_money.SendMoneyCommand;
import com.jonathan.modern_design.account.application.send_money.SendMoneyUseCase;
import com.jonathan.modern_design.account.application.update_account.UpdateAccountUseCase;
import com.jonathan.modern_design.account.domain.Account;
import com.jonathan.modern_design.account.infraestructure.persistence.AccountRepository;
import com.jonathan.modern_design.common.BeanClass;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@BeanClass
@RequiredArgsConstructor
public class AccountFacade implements SendMoneyUseCase, FindAccountUseCase, UpdateAccountUseCase {
    private final AccountRepository accountRepository;
    private final SendMoneyUseCase sendMoneyUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;

    @Override
    public boolean sendMoney(SendMoneyCommand command) {
        return sendMoneyUseCase.sendMoney(command);
    }

    @Override
    public Optional<Account> findOne(Long accountId) {
        return accountRepository.findOne(accountId);
    }

    @Override
    public void update(Account account) {
        updateAccountUseCase.update(account);
    }
}
