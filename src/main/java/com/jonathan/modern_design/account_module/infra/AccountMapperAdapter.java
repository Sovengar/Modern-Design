package com.jonathan.modern_design.account_module.infra;

import com.jonathan.modern_design._infra.config.annotations.BeanClass;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.infra.persistence.AccountEntity;
import org.mapstruct.factory.Mappers;

@BeanClass
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

    @Override
    public void updateAccountEntity(final Account account, AccountEntity accountEntity) {
        mapStructInstance.updateAccountEntity(account, accountEntity);
    }
}
