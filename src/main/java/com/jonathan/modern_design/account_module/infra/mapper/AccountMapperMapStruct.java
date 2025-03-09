package com.jonathan.modern_design.account_module.infra.mapper;

import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountAddress;
import com.jonathan.modern_design.account_module.domain.model.AccountId;
import com.jonathan.modern_design.account_module.domain.model.AccountMoney;
import com.jonathan.modern_design.account_module.domain.model.AccountNumber;
import com.jonathan.modern_design.account_module.infra.persistence.AccountEntity;
import com.jonathan.modern_design.user_module.user.domain.model.User;
import com.jonathan.modern_design.user_module.user.infra.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.UUID;

@Mapper(uses = {UserMapper.class})
public interface AccountMapperMapStruct {
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

    default String mapAccountNumber(AccountNumber accountNumber) {
        return accountNumber.getValue();
    }

    default AccountNumber mapAccountNumber(String accountNumber) {
        return AccountNumber.of(accountNumber);
    }

    default AccountId mapId(Long id) {
        return new AccountId(id);
    }

    default Long mapId(AccountId id) {
        return id == null ? null : id.getAccountId();
    }

    default User.ID mapUserId(UUID uuid) {
        return new User.ID(uuid);
    }
}
