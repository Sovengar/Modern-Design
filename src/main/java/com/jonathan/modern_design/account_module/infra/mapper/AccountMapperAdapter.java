package com.jonathan.modern_design.account_module.infra.mapper;

import com.jonathan.modern_design._infra.config.annotations.Inyectable;
import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountAddress;
import com.jonathan.modern_design.account_module.domain.model.AccountId;
import com.jonathan.modern_design.account_module.domain.model.AccountMoney;
import com.jonathan.modern_design.account_module.domain.model.AccountNumber;
import com.jonathan.modern_design.account_module.dtos.AccountResource;
import com.jonathan.modern_design.account_module.infra.persistence.AccountEntity;
import com.jonathan.modern_design.user_module.domain.User;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Inyectable
public class AccountMapperAdapter implements AccountMapper {
    AccountMapperMapStruct mapStructInstance = Mappers.getMapper(AccountMapperMapStruct.class);

    @Override
    public Account toAccount(AccountEntity accountEntity) {
        return mapStructInstance.toAccount(accountEntity);
    }

    @Override
    public Account toAccount(final AccountResource accountResource) {
        AccountId accountId = null;
        AccountNumber accountNumber = AccountNumber.of(accountResource.accountNumber());
        AccountMoney money = AccountMoney.of(accountResource.amount(), Currency.valueOf(accountResource.currency()));
        AccountAddress address = null;
        User.UserId userId = accountResource.userId();
        LocalDateTime dateOfLastTransaction = null;
        boolean active = true;
        return Account.builder()
                .accountId(accountId)
                .accountNumber(accountNumber)
                .money(money)
                .address(address)
                .userId(userId)
                .dateOfLastTransaction(dateOfLastTransaction)
                .active(active)
                .build();
        //TODO
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
