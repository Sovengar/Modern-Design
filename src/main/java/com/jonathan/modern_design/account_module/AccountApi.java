package com.jonathan.modern_design.account_module;

import com.jonathan.modern_design.account_module.domain.model.AccountNumber;
import com.jonathan.modern_design.account_module.dtos.AccountCreatorCommand;
import com.jonathan.modern_design.account_module.dtos.AccountResource;
import com.jonathan.modern_design.account_module.dtos.DepositCommand;
import com.jonathan.modern_design.account_module.dtos.TransferMoneyCommand;
import com.jonathan.modern_design.account_module.infra.query.AccountSearchCriteria;

import java.util.List;

public interface AccountApi {
    void transferMoney(final TransferMoneyCommand command);

    AccountResource findOne(final String accountNumber);

    List<AccountResource> search(final AccountSearchCriteria filters);

    void update(AccountResource dto);

    AccountNumber createAccount(final AccountCreatorCommand command);

    void deposit(final DepositCommand command);
}
