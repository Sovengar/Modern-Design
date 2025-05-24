package jonathan.modern_design.account_module.infra;

import jonathan.modern_design._shared.country.CountriesInventory;
import jonathan.modern_design._shared.country.CountriesInventoryStub;
import jonathan.modern_design.account_module.api.AccountApi;
import jonathan.modern_design.account_module.application.CreateAccount;
import jonathan.modern_design.account_module.application.Deposit;
import jonathan.modern_design.account_module.application.GenericUpdateAccount;
import jonathan.modern_design.account_module.application.TransferMoney;
import jonathan.modern_design.account_module.application.queries.FindAccount;
import jonathan.modern_design.account_module.domain.services.AccountValidator;
import jonathan.modern_design.account_module.domain.store.AccountRepo;
import jonathan.modern_design.account_module.domain.store.AccountRepoInMemory;
import jonathan.modern_design.account_module.domain.store.TransactionRepo;
import jonathan.modern_design.user.api.UserApi;
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
            CountriesInventory countriesInventory
    ) {
        AccountValidator accountValidator = new AccountValidator();

        return new AccountApi.Internal(
                findAccount,
                new TransferMoney(accountRepo, transactionRepo, accountValidator),
                new CreateAccount(accountRepo, userFacade, countriesInventory),
                new GenericUpdateAccount(accountRepo),
                new Deposit(accountRepo, transactionRepo)
        );
    }

    @Profile("test")
    public AccountApi accountApi(UserApi userApi) {
        //For Unit testing
        FindAccount findAccount = null; //TODO
        final CountriesInventory countriesInventory = new CountriesInventoryStub();

        TransactionRepo transactionRepo = null; //TODO CREARLE INMEMORY

        return accountApi(accountRepo, transactionRepo, findAccount, userApi, countriesInventory);
    }

    @Profile("test")
    public AccountRepo getAccountRepo() {
        //For Unit testing
        return accountRepo;
    }
}
