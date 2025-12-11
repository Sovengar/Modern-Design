package jonathan.modern_design.banking;

import jakarta.persistence.EntityManager;
import jonathan.modern_design.banking.domain.AccountDsl;
import jonathan.modern_design.banking.domain.models.Account;
import jonathan.modern_design.banking.domain.models.AccountEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class BankingDsl {
    @Autowired
    protected EntityManager entityManager;

    protected Account givenAnAccountWithBalance(double balance, String accountNumber) {
        var account = AccountDsl.givenAnAccountWithBalance(balance, accountNumber);
        var accountEntity = new AccountEntity(account);
        entityManager.persist(accountEntity);
        return account;
    }

    protected Account givenAnAccountWithBalance(double balance) {
        var account = AccountDsl.givenAnAccountWithBalance(balance);
        var accountEntity = new AccountEntity(account);
        entityManager.persist(accountEntity);
        return account;
    }

    protected Account givenAnEmptyAccount() {
        var account = AccountDsl.givenAnEmptyAccount();
        var accountEntity = new AccountEntity(account);
        entityManager.persist(accountEntity);
        return account;
    }

    protected Account givenARandomAccountWithBalance(double balance) {
        return givenAnAccountWithBalance(balance, UUID.randomUUID().toString());
    }
}
