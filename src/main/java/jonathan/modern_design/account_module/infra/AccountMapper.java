package jonathan.modern_design.account_module.infra;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.AccountEntity;

//This mapper is unsued, just to show how it can be used together with mapStruct
//private final AccountMapperStruct mapStructInstance; with componentModel="spring" or Mappers.getMapper(AccountMapperStruct.class)
public interface AccountMapper {
    Account toDomain(final AccountEntity accountEntity);

    void updateEntity(AccountEntity accountEntity, Account account);
}

@Injectable
class AccountMapperAdapter implements AccountMapper {

    @Override
    public Account toDomain(AccountEntity accountEntity) {
        return accountEntity.toDomain();
    }

    @Override
    public void updateEntity(AccountEntity accountEntity, Account account) {
        accountEntity.accountNumber(account.accountAccountNumber().accountNumber());
        accountEntity.balance(account.money().amount());
        accountEntity.currency(account.money().currency());
        accountEntity.address(account.address().toString());
        accountEntity.userId(account.userId());
        accountEntity.dateOfLastTransaction(account.dateOfLastTransaction());
        accountEntity.active(account.active());
    }
}
