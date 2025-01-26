package com.jonathan.modern_design.account.infraestructure;

import com.jonathan.modern_design.account.domain.Account;
import com.jonathan.modern_design.account.infraestructure.persistence.AccountEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mappings({})
    Account toAccount(final AccountEntity accountEntity);

    Iterable<Account> toAccounts(Iterable<AccountEntity> accountEntity);

    @InheritInverseConfiguration
    AccountEntity toAccountEnity (final Account account);
}
