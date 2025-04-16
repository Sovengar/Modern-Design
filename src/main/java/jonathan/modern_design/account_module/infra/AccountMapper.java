package jonathan.modern_design.account_module.infra;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.vo.AccountId;
import jonathan.modern_design.account_module.domain.vo.AccountMoney;
import jonathan.modern_design.account_module.domain.vo.AccountNumber;
import jonathan.modern_design.account_module.dtos.AccountDto;
import jonathan.modern_design.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface AccountMapper {
    Account toAccount(final AccountEntity accountEntity);

    Account toAccount(final AccountDto accountDto);

    Iterable<Account> toAccounts(Iterable<AccountEntity> accountEntity);

    AccountEntity toAccountEntity(final Account account);

    void updateAccountEntity(Account account, AccountEntity accountEntity);
}

@Mapper
interface AccountMapperMapStruct {
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
        return money.amount();
    }

    @Named("mapCurrency")
    default Currency mapCurrency(AccountMoney money) {
        return money.currency();
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
        return accountNumber.accountNumber();
    }

    default AccountNumber mapAccountNumber(String accountNumber) {
        return AccountNumber.of(accountNumber);
    }

    default AccountId mapId(Long id) {
        return new AccountId(id);
    }

    default Long mapId(AccountId id) {
        return id == null ? null : id.id();
    }

    default User.UserId mapUserId(UUID uuid) {
        return new User.UserId(uuid);
    }
}

@Injectable
class AccountMapperAdapter implements AccountMapper {
    AccountMapperMapStruct mapStructInstance = Mappers.getMapper(AccountMapperMapStruct.class);

    @Override
    public Account toAccount(AccountEntity accountEntity) {
        return mapStructInstance.toAccount(accountEntity);
    }

    @Override
    public Account toAccount(final AccountDto accountDto) {
        AccountId accountId = null;
        AccountNumber accountNumber = AccountNumber.of(accountDto.accountNumber());
        AccountMoney money = AccountMoney.of(accountDto.balance(), Currency.valueOf(accountDto.currency()));
        AccountAddress address = null;
        User.UserId userId = accountDto.userId();
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
