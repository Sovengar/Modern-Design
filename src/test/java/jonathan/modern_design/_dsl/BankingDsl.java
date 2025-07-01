package jonathan.modern_design._dsl;

import jonathan.modern_design._shared.vo.Money;
import jonathan.modern_design.banking.api.BankingApi;
import jonathan.modern_design.banking.application.Deposit;
import jonathan.modern_design.banking.domain.models.Account;
import jonathan.modern_design.banking.domain.models.AccountEntity;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import jonathan.modern_design.banking.infra.store.spring.AccountRepoSpringDataJPA;
import org.junit.jupiter.api.Assertions;

import static jonathan.modern_design._dsl.AccountStub.CreateAccountMother.createAccountCommand;

public class BankingDsl {
    public static Account givenAnAccountWithMoney(Money money, BankingApi bankingApi, AccountRepo repo) {
        Assertions.assertNotNull(money.getCurrency());
        var accountNumber = bankingApi.createAccount(createAccountCommand(money.getCurrency().getCode())).getAccountNumber();

        if (money.checkPositive()) {
            bankingApi.deposit(new Deposit.Command(accountNumber, money.getBalance(), money.getCurrency()));
        }

        return repo.findByAccNumberOrElseThrow(accountNumber);
    }

    public static AccountEntity givenAnAccount(AccountRepoSpringDataJPA repo) {
        var accountEntity = new AccountEntity(AccountStub.AccountMother.sourceAccountEmpty());
        repo.save(accountEntity);
        return accountEntity;
    }
}
