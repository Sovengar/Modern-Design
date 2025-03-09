package com.jonathan.modern_design.account_module.infra.mapper;

import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.dtos.AccountResource;
import com.jonathan.modern_design.account_module.infra.persistence.AccountEntity;

public interface AccountMapper {
    Account toAccount(final AccountEntity accountEntity);

    Account toAccount(final AccountResource accountResource);

    Iterable<Account> toAccounts(Iterable<AccountEntity> accountEntity);

    AccountEntity toAccountEntity(final Account account);

    void updateAccountEntity(Account account, AccountEntity accountEntity);
}
