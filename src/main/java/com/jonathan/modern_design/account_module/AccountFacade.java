package com.jonathan.modern_design.account_module;

import com.jonathan.modern_design._infra.config.annotations.BeanClass;
import com.jonathan.modern_design.account_module.application.AccountResource;
import com.jonathan.modern_design.account_module.application.CreateAccountUseCase;
import com.jonathan.modern_design.account_module.application.DepositUseCase;
import com.jonathan.modern_design.account_module.application.FindAccountUseCase;
import com.jonathan.modern_design.account_module.application.TransferMoneyUseCase;
import com.jonathan.modern_design.account_module.application.UpdateAccountUseCase;
import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.infra.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@BeanClass
@RequiredArgsConstructor

public class AccountFacade implements TransferMoneyUseCase, FindAccountUseCase, UpdateAccountUseCase, CreateAccountUseCase, DepositUseCase {
    private final AccountRepository repository;
    private final TransferMoneyUseCase transferMoneyUseCase;
    private final CreateAccountUseCase createAccountUseCase;
    private final AccountMapper accountMapper;

    @Transactional
    @Override
    public void transferMoney(final TransferMoneyCommand command) {
        transferMoneyUseCase.transferMoney(command);
    }

    // CQRS, we can skip service layer and access directly to repository
    @Override
    public AccountResource findOne(final String accountNumber) {
        final var account = repository.findOneOrElseThrow(accountNumber);
        return new AccountResource(account);
    }

    @Transactional
    @Override
    public void update(AccountResource accountResource) {
        var account = accountMapper.toAccount(accountResource);
        update(account);
    }

    @Transactional
    @Override
    public Account createAccount(final CreateAccountCommand command) {
        return createAccountUseCase.createAccount(command);
    }

    @Transactional
    @Override
    public void deposit(final DepositCommand command) {
        var account = repository.findOne(command.accountNumber()).orElseThrow();
        account.add(command.amount(), command.currency());
        update(account);
    }

    private void update(Account account) {
        //If update ends up with more logic, extract to a service and make other services depend on it, i.e. transferMoney
        repository.update(account);
    }
}
