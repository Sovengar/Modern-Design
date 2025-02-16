package com.jonathan.modern_design.account_module.infra;

import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountAddress;
import com.jonathan.modern_design.account_module.domain.model.AccountMoney;
import com.jonathan.modern_design.account_module.infra.persistence.AccountEntity;
import com.jonathan.modern_design.user_module.infra.UserMapper;
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

    default AccountMoney mapMoney(BigDecimal balance, Currency currency) {
        return AccountMoney.of(balance, currency);
    }

    @Named("mapBalance")
    default BigDecimal mapBalance(AccountMoney money) {
        return money.getAmount();
    }

    @Named("mapCurrency")
    default Currency mapCurrency(AccountMoney money) {
        return money.getCurrency();
    }

    @Named("mapAddress")
    default AccountAddress mapAddress(String address) {
        return AccountAddress.of(address);
    }

    @Named("mapAddress")
    default String mapAddress(AccountAddress address) {
        return address.toString();
    }

    default Account.AccountId mapId(Long id) {
        return new Account.AccountId(id);
    }

    default Long mapId(Account.AccountId id) {
        return id == null ? null : id.value();
    }
}
