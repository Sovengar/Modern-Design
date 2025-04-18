package jonathan.modern_design.account_module.infra;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.AccountEntity;

public interface AccountMapper {
    void updateEntity(AccountEntity accountEntity, Account account);
}

@Injectable
class AccountMapperAdapter implements AccountMapper {
    //You can add mapStruct to help mapping
    //private final AccountMapperStruct mapStructInstance; with componentModel="spring" or Mappers.getMapper(AccountMapperStruct.class)

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
