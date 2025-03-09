package com.jonathan.modern_design.account_module;

import com.jonathan.modern_design._infra.config.annotations.BeanClass;
import com.jonathan.modern_design.account_module.application.AccountCreator;
import com.jonathan.modern_design.account_module.application.DepositUseCase;
import com.jonathan.modern_design.account_module.application.FindAccountUseCase;
import com.jonathan.modern_design.account_module.application.TransferMoneyUseCase;
import com.jonathan.modern_design.account_module.application.UpdateAccountUseCase;
import com.jonathan.modern_design.account_module.domain.AccountRepo;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountNumber;
import com.jonathan.modern_design.account_module.dtos.AccountCreatorCommand;
import com.jonathan.modern_design.account_module.dtos.AccountResource;
import com.jonathan.modern_design.account_module.infra.mapper.AccountMapper;
import com.jonathan.modern_design.account_module.infra.query.AccountSearchCriteria;
import com.jonathan.modern_design.account_module.infra.query.AccountSearchRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@BeanClass
@RequiredArgsConstructor

public class AccountFacade implements TransferMoneyUseCase, FindAccountUseCase, UpdateAccountUseCase, DepositUseCase {
    private final AccountRepo repository;
    private final AccountSearchRepo accountSearchRepo;
    private final TransferMoneyUseCase transferMoneyUseCase;
    private final AccountCreator accountCreator;
    private final AccountMapper accountMapper;

    @Transactional
    @Override
    public void transferMoney(final TransferMoneyCommand command) {
        transferMoneyUseCase.transferMoney(command);
    }

    //region CQRS, we can skip service layer and access directly to repository
    @Override
    public AccountResource findOne(final String accountNumber) {
        final var account = repository.findOneOrElseThrow(accountNumber);
        return new AccountResource(account);
    }

    public List<AccountResource> search(final AccountSearchCriteria filters) {
        return accountSearchRepo.search(filters);
        //TODO Tener varios search por caso de uso y n mappers, n projections, ...
    }
    //endregion

    @Transactional
    @Override
    public void update(AccountResource dto) {
        var account = accountMapper.toAccount(dto);
        update(account);
    }

    @Transactional
    public AccountNumber createAccount(final AccountCreatorCommand command) {
        return accountCreator.createAccount(command);
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
