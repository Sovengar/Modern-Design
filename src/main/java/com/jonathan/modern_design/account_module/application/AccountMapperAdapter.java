package com.jonathan.modern_design.account_module.application;

import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.infraestructure.persistence.AccountEntity;
import org.mapstruct.factory.Mappers;

public class AccountMapperAdapter implements AccountMapper {
    AccountMapper mapStructInstance = Mappers.getMapper(AccountMapper.class);

    @Override
    public Account toAccount(AccountEntity accountEntity) {
        return mapStructInstance.toAccount(accountEntity);
    }

    @Override
    public Iterable<Account> toAccounts(Iterable<AccountEntity> accountEntity) {
        return mapStructInstance.toAccounts(accountEntity);
    }

    @Override
    public AccountEntity toAccountEntity(Account account) {
        return mapStructInstance.toAccountEntity(account);
    }
}
