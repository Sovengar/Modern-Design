package jonathan.modern_design.account_module.infra;

import jonathan.modern_design._shared.country.CountriesInventory;
import jonathan.modern_design._shared.country.CountriesInventoryStub;
import jonathan.modern_design.account_module.AccountApi;
import jonathan.modern_design.account_module.application.AccountCRUDUpdater;
import jonathan.modern_design.account_module.application.AccountCreator;
import jonathan.modern_design.account_module.application.AccountFinder;
import jonathan.modern_design.account_module.application.Deposit;
import jonathan.modern_design.account_module.application.MoneyTransfer;
import jonathan.modern_design.account_module.application.search.SearchAccount;
import jonathan.modern_design.account_module.domain.repos.AccountInMemoryRepo;
import jonathan.modern_design.account_module.domain.repos.AccountRepo;
import jonathan.modern_design.account_module.domain.services.AccountValidator;
import jonathan.modern_design.user.UserApi;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AccountingConfig {
    final AccountRepo accountRepo = new AccountInMemoryRepo();

    public AccountApi accountApi(
            AccountRepo accountRepo,
            AccountFinder accountFinder,
            SearchAccount searchAccount,
            UserApi userFacade,
            CountriesInventory countriesInventory
    ) {
        AccountValidator accountValidator = new AccountValidator();

        return new AccountApi.AccountInternalApi(
                accountFinder,
                searchAccount,
                new MoneyTransfer(accountRepo, accountValidator),
                new AccountCreator(accountRepo, userFacade, countriesInventory),
                new AccountCRUDUpdater(accountRepo),
                new Deposit(accountRepo)
        );
    }

    @Profile("test")
    public AccountApi accountApi(UserApi userApi) {
        //For Unit testing
        SearchAccount searchAccount = null;
        AccountFinder accountFinder = null;
        final CountriesInventory countriesInventory = new CountriesInventoryStub();

        return accountApi(accountRepo, accountFinder, searchAccount, userApi, countriesInventory);
    }

    @Profile("test")
    public AccountRepo getAccountRepo() {
        //For Unit testing
        return accountRepo;
    }
}
