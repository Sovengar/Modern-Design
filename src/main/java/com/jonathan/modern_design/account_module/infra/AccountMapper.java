package com.jonathan.modern_design.account_module.infra;

import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountAddressVO;
import com.jonathan.modern_design.account_module.domain.model.AccountMoneyVO;
import com.jonathan.modern_design.account_module.infra.persistence.AccountEntity;
import com.jonathan.modern_design.shared.Currency;
import com.jonathan.modern_design.user_module.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(uses = {UserMapper.class})
public interface AccountMapper {

    @Mapping(source = "address", target = "address", qualifiedByName = "mapAddress")
    @Mapping(target = "money", expression = "java(mapMoney(accountEntity.getBalance(), accountEntity.getCurrency()))")
    Account toAccount(final AccountEntity accountEntity);

    Iterable<Account> toAccounts(Iterable<AccountEntity> accountEntity);

    @Mapping(source = "address", target = "address", qualifiedByName = "mapAddress")
    @Mapping(source = "money", target = "balance", qualifiedByName = "mapBalance")
    @Mapping(source = "money", target = "currency", qualifiedByName = "mapCurrency")
    AccountEntity toAccountEntity(final Account account);

    @Mapping(source = "address", target = "address", qualifiedByName = "mapAddress")
    @Mapping(source = "money", target = "balance", qualifiedByName = "mapBalance")
    @Mapping(source = "money", target = "currency", qualifiedByName = "mapCurrency")
    void updateAccountEntity(Account account, @MappingTarget AccountEntity accountEntity);

    default AccountMoneyVO mapMoney(BigDecimal balance, Currency currency) {
        return AccountMoneyVO.of(balance, currency);
    }

    @Named("mapBalance")
    default BigDecimal mapBalance(AccountMoneyVO money) {
        return money.getBalance();
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
