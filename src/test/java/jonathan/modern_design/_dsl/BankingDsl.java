package jonathan.modern_design._dsl;

import jakarta.persistence.EntityManager;
import jonathan.modern_design._shared.domain.vo.Money;
import jonathan.modern_design.banking.api.BankingApi;
import jonathan.modern_design.banking.application.Deposit;
import jonathan.modern_design.banking.domain.models.Account;
import jonathan.modern_design.banking.domain.models.AccountEntity;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import static jonathan.modern_design._dsl.AccountStub.AccountMother.accountWithBalance;
import static jonathan.modern_design._dsl.AccountStub.AccountMother.emptyAccount;
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

    protected Account givenAnAccountWithBalance(double balance, String accountNumber) {
        var account = accountWithBalance(balance, accountNumber);
        var accountEntity = new AccountEntity(account);
        entityManager.persist(accountEntity);
        return account;
    }

    protected Account givenAnAccountWithBalance(double balance) {
        var account = accountWithBalance(balance);
        var accountEntity = new AccountEntity(account);
        entityManager.persist(accountEntity);
        return account;
    }

    protected Account givenAnEmptyAccount() {
        var account = emptyAccount();
        var accountEntity = new AccountEntity(account);
        entityManager.persist(accountEntity);
        return account;
    }
}
