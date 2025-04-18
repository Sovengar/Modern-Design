package jonathan.modern_design.account_module.infra;

import jonathan.modern_design._shared.country.CountriesInventory;
import jonathan.modern_design._shared.country.CountriesInventoryStub;
import jonathan.modern_design.account_module.AccountApi;
import jonathan.modern_design.account_module.application.AccountCRUDUpdater;
import jonathan.modern_design.account_module.application.AccountCreator;
import jonathan.modern_design.account_module.application.Deposit;
import jonathan.modern_design.account_module.application.MoneyTransfer;
import jonathan.modern_design.account_module.domain.AccountRepo;
import jonathan.modern_design.account_module.domain.services.AccountValidator;
import jonathan.modern_design.user.UserApi;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AccountingConfig {
    final AccountRepo accountRepo = new AccountInMemoryRepo();

    public AccountApi accountApi(AccountRepo accountRepo, AccountSearchRepo accountSearcher, UserApi userFacade, CountriesInventory countriesInventory) {
        AccountValidator accountValidator = new AccountValidator();

        return new AccountFacade(
                accountRepo,
                accountSearcher,
                new MoneyTransfer(accountRepo, accountValidator),
                new AccountCreator(accountRepo, userFacade, countriesInventory),
                new AccountCRUDUpdater(accountRepo),
                new Deposit(accountRepo),
                new AccountMapperAdapter()
        );
    }

    @Profile("test")
    public AccountApi accountApi(UserApi userApi) {
        //For Unit testing
        AccountSearchRepo accountSearcher = null;
        final CountriesInventory countriesInventory = new CountriesInventoryStub();

        return accountApi(accountRepo, accountSearcher, userApi, countriesInventory);
    }

    @Profile("test")
    public AccountRepo getAccountRepo() {
        //For Unit testing
        return accountRepo;
    }
}
