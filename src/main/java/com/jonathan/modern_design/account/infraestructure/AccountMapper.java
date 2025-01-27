package com.jonathan.modern_design.account.infraestructure;

import com.jonathan.modern_design.account.domain.Account;
import com.jonathan.modern_design.account.domain.AccountId;
import com.jonathan.modern_design.account.domain.AccountMoneyVO;
import com.jonathan.modern_design.account.infraestructure.persistence.AccountEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    Account toAccount(final AccountEntity accountEntity);

    Iterable<Account> toAccounts(Iterable<AccountEntity> accountEntity);

    @InheritInverseConfiguration
    AccountEntity toAccountEnity (final Account account);

    default AccountId mapAmount(Long value) {
        return AccountId.of(value);
    }

    default AccountMoneyVO mapMoney(BigDecimal amount) {
        return AccountMoneyVO.of(amount, "MXN"); //TODO FIX
    }
}
