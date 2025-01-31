package com.jonathan.modern_design.account.infraestructure;

import com.jonathan.modern_design.account.domain.Account;
import com.jonathan.modern_design.account_module.infraestructure.persistence.AccountEntity;
import java.util.ArrayList;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-27T01:15:55+0100",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 21.0.4 (Eclipse Adoptium)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public Account toAccount(AccountEntity accountEntity) {
        if ( accountEntity == null ) {
            return null;
        }

        Account.AccountBuilder account = Account.builder();

        account.id( mapAmount( accountEntity.getId() ) );

        return account.build();
    }

    @Override
    public Iterable<Account> toAccounts(Iterable<AccountEntity> accountEntity) {
        if ( accountEntity == null ) {
            return null;
        }

        ArrayList<Account> iterable = new ArrayList<Account>();
        for ( AccountEntity accountEntity1 : accountEntity ) {
            iterable.add( toAccount( accountEntity1 ) );
        }

        return iterable;
    }

    @Override
    public AccountEntity toAccountEnity(Account account) {
        if ( account == null ) {
            return null;
        }

        AccountEntity.AccountEntityBuilder accountEntity = AccountEntity.builder();

        accountEntity.id( account.getId() );

        return accountEntity.build();
    }
}
