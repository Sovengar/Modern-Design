package jonathan.modern_design._dsl;

import jakarta.persistence.EntityManager;
import jonathan.modern_design._shared.domain.vo.Money;
import jonathan.modern_design.banking.api.BankingApi;
import jonathan.modern_design.banking.application.Deposit;
import jonathan.modern_design.banking.domain.models.Account;
import jonathan.modern_design.banking.domain.models.AccountEntity;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import jonathan.modern_design.banking.infra.store.repositories.spring_jpa.AccountSpringJpaRepo;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import static jonathan.modern_design._dsl.AccountStub.AccountMother.sourceAccountEmpty;
import static jonathan.modern_design._dsl.AccountStub.AccountMother.sourceAccountWithBalance;
import static jonathan.modern_design._dsl.AccountStub.CreateAccountMother.createAccountCommand;

public class BankingDsl {
    @Autowired
    private EntityManager entityManager;

    public static Account givenAnAccountWithMoney(Money money, BankingApi bankingApi, AccountRepo repo) {
        Assertions.assertNotNull(money.getCurrency());
        var accountNumber = bankingApi.createAccount(createAccountCommand(money.getCurrency().getCode())).getAccountNumber();

        if (money.checkPositive()) {
            bankingApi.deposit(new Deposit.Command(accountNumber, money.getBalance(), money.getCurrency()));
        }

        return repo.findByAccNumberOrElseThrow(accountNumber);
    }

    public static AccountEntity givenAnAccount(AccountSpringJpaRepo repo) {
        var accountEntity = new AccountEntity(sourceAccountEmpty());
        repo.save(accountEntity);
        return accountEntity;
    }

    protected Account givenAnAccountWithMoney(Money money) {
        var account = sourceAccountWithBalance(money.getBalance().toBigInteger().doubleValue());
        var accountEntity = new AccountEntity(account);
        entityManager.persist(accountEntity);
        return account;
    }

    protected Account givenAnAccountWithBalance(double balance) {
        var account = sourceAccountWithBalance(balance);
        var accountEntity = new AccountEntity(account);
        entityManager.persist(accountEntity);
        return account;
    }

    protected Account givenAnEmptyAccount() {
        var account = sourceAccountEmpty();
        var accountEntity = new AccountEntity(account);
        entityManager.persist(accountEntity);
        return account;
    }
}
