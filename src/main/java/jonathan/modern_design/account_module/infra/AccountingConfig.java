package jonathan.modern_design.account_module.infra;

import jonathan.modern_design._shared.country.CountriesInventory;
import jonathan.modern_design._shared.country.CountriesInventoryStub;
import jonathan.modern_design.account_module.api.AccountApi;
import jonathan.modern_design.account_module.application.CreateAccount;
import jonathan.modern_design.account_module.application.Deposit;
import jonathan.modern_design.account_module.application.FindAccount;
import jonathan.modern_design.account_module.application.TransferMoney;
import jonathan.modern_design.account_module.application.UpdateAccountCRUD;
import jonathan.modern_design.account_module.domain.services.AccountValidator;
import jonathan.modern_design.account_module.domain.store.AccountRepo;
import jonathan.modern_design.account_module.domain.store.AccountRepoInMemory;
import jonathan.modern_design.user.api.UserApi;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AccountingConfig {
    final AccountRepo accountRepo = new AccountRepoInMemory();

    public AccountApi accountApi(
            AccountRepo accountRepo,
            FindAccount findAccount,
            UserApi userFacade,
            CountriesInventory countriesInventory
    ) {
        AccountValidator accountValidator = new AccountValidator();

        return new AccountApi.AccountInternalApi(
                findAccount,
                new TransferMoney(accountRepo, accountValidator),
                new CreateAccount(accountRepo, userFacade, countriesInventory),
                new UpdateAccountCRUD(accountRepo),
                new Deposit(accountRepo)
        );
    }

    @Profile("test")
    public AccountApi accountApi(UserApi userApi) {
        //For Unit testing
        FindAccount findAccount = null; //TODO
        final CountriesInventory countriesInventory = new CountriesInventoryStub();

        return accountApi(accountRepo, findAccount, userApi, countriesInventory);
    }

    @Profile("test")
    public AccountRepo getAccountRepo() {
        //For Unit testing
        return accountRepo;
    }
}
