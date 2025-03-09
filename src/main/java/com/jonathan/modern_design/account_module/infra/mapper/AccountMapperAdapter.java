package com.jonathan.modern_design.account_module.infra.mapper;

import com.jonathan.modern_design._infra.config.annotations.BeanClass;
import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountMoney;
import com.jonathan.modern_design.account_module.domain.model.AccountNumber;
import com.jonathan.modern_design.account_module.dtos.AccountResource;
import com.jonathan.modern_design.account_module.infra.persistence.AccountEntity;
import org.mapstruct.factory.Mappers;

@BeanClass
public class AccountMapperAdapter implements AccountMapper {
    AccountMapperMapStruct mapStructInstance = Mappers.getMapper(AccountMapperMapStruct.class);

    @Override
    public Account toAccount(AccountEntity accountEntity) {
        return mapStructInstance.toAccount(accountEntity);
    }

    @Override
    public Account toAccount(final AccountResource accountResource) {
        return Account.builder()
                .accountNumber(AccountNumber.of(accountResource.accountNumber()))
                .money(AccountMoney.of(accountResource.amount(), Currency.valueOf(accountResource.currency())))
                .userId(accountResource.userId()).build();
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
