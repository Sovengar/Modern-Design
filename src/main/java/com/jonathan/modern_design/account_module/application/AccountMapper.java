package com.jonathan.modern_design.account_module.application;

import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountAddressVO;
import com.jonathan.modern_design.account_module.domain.model.AccountMoneyVO;
import com.jonathan.modern_design.account_module.infraestructure.persistence.AccountEntity;
import com.jonathan.modern_design.shared.Currency;
import com.jonathan.modern_design.user_module.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(uses = {UserMapper.class})
public interface AccountMapper {

    @Mapping(source = "address", target = "address", qualifiedByName = "mapAddress")
    @Mapping(target = "money", expression = "java(mapMoney(accountEntity.getAmount(), accountEntity.getCurrency()))")
    Account toAccount(final AccountEntity accountEntity);

    Iterable<Account> toAccounts(Iterable<AccountEntity> accountEntity);

    @Mapping(source = "address", target = "address", qualifiedByName = "mapAddress")
    @Mapping(source = "money", target = "amount", qualifiedByName = "mapAmount")
    @Mapping(source = "money", target = "currency", qualifiedByName = "mapCurrency")
    AccountEntity toAccountEntity(final Account account);

    default AccountMoneyVO mapMoney(BigDecimal amount, Currency currency) {
        return AccountMoneyVO.of(amount, currency);
    }

    @Named("mapAmount")
    default BigDecimal mapAmount(AccountMoneyVO money) {
        return money.getAmount();
    }

    @Named("mapCurrency")
    default Currency mapCurrency(AccountMoneyVO money) {
        return money.getCurrency();
    }

    @Named("mapAddress")
    default AccountAddressVO mapAddress(String address) {
        return AccountAddressVO.of(address);
    }

    @Named("mapAddress")
    default String mapAddress(AccountAddressVO address) {
        return address.toString();
    }
}
