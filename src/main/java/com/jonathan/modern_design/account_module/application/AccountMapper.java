package com.jonathan.modern_design.account_module.application;

import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountMoneyVO;
import com.jonathan.modern_design.account_module.infraestructure.persistence.AccountEntity;
import com.jonathan.modern_design.shared.Currency;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.math.BigDecimal;

@Mapper
public interface AccountMapper {
    Account toAccount(final AccountEntity accountEntity);

    Iterable<Account> toAccounts(Iterable<AccountEntity> accountEntity);

    @InheritInverseConfiguration
    AccountEntity toAccountEntity(final Account account);

    default AccountMoneyVO mapMoney(BigDecimal amount) {
        return AccountMoneyVO.of(amount, Currency.EURO); //TODO FIX
    }
}
