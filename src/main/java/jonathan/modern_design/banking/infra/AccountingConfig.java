package jonathan.modern_design.banking.infra;

import jonathan.modern_design.auth.api.UserApi;
import jonathan.modern_design.banking.api.AccountApi;
import jonathan.modern_design.banking.application.CreateAccount;
import jonathan.modern_design.banking.application.Deposit;
import jonathan.modern_design.banking.application.GenericUpdateAccount;
import jonathan.modern_design.banking.application.TransferMoney;
import jonathan.modern_design.banking.application.queries.FindAccount;
import jonathan.modern_design.banking.domain.policies.AccountNumberGenerator;
import jonathan.modern_design.banking.domain.services.AccountNumberDefaultGenerator;
import jonathan.modern_design.banking.domain.services.AccountValidator;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import jonathan.modern_design.banking.domain.store.AccountRepoInMemory;
import jonathan.modern_design.banking.domain.store.TransactionRepo;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AccountingConfig {
    final AccountRepo accountRepo = new AccountRepoInMemory();

    public AccountApi accountApi(
            AccountRepo accountRepo,
            TransactionRepo transactionRepo,
            FindAccount findAccount,
            UserApi userFacade,
            ApplicationEventPublisher applicationEventPublisher,
            AccountNumberGenerator accountNumberGenerator
    ) {
        AccountValidator accountValidator = new AccountValidator();

        return new AccountApi.Internal(
                findAccount,
                new TransferMoney(accountRepo, transactionRepo, accountValidator),
                new CreateAccount(accountRepo, userFacade, applicationEventPublisher, accountNumberGenerator),
                new GenericUpdateAccount(accountRepo),
                new Deposit(accountRepo, transactionRepo)
        );
    }

    @Profile("test")
    public AccountApi accountApi(UserApi userApi) {
        //For Unit testing
        FindAccount findAccount = null; //TODO
        TransactionRepo transactionRepo = null; //TODO CREARLE INMEMORY
        ApplicationEventPublisher applicationEventPublisher = null; //TODO

        AccountNumberGenerator accountNumberGenerator = new AccountNumberDefaultGenerator();
        return accountApi(accountRepo, transactionRepo, findAccount, userApi, applicationEventPublisher, accountNumberGenerator);
    }

    @Profile("test")
    public AccountRepo getAccountRepo() {
        //For Unit testing
        return accountRepo;
    }
}
