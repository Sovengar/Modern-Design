package jonathan.modern_design.account_module.infra;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.AccountEntity;
import jonathan.modern_design.account_module.dtos.AccountDto;

//This mapper is unsued, just to show how it can be used together with mapStruct
//private final AccountMapperStruct mapStructInstance; with componentModel="spring" or Mappers.getMapper(AccountMapperStruct.class)

public interface AccountMapper {
    Account toAccount(final AccountEntity accountEntity);

    AccountEntity toAccountEntity(final Account account);

    Account updateAccount(Account account, final AccountDto accountDto);
}

@Injectable
class AccountMapperAdapter implements AccountMapper {

    @Override
    public Account toAccount(AccountEntity accountEntity) {
        return accountEntity.toDomain();
    }

    @Override
    public AccountEntity toAccountEntity(Account account) {
        return account.toEntity();
    }

    @Override
    public Account updateAccount(Account account, final AccountDto dto) {
        return null;
    }
}
