package jonathan.modern_design.banking.infra;

import jonathan.modern_design._shared.country.CountriesCatalog;
import jonathan.modern_design._shared.country.CountriesCatalogStub;
import jonathan.modern_design.auth.api.AuthApi;
import jonathan.modern_design.banking.api.BankingApi;
import jonathan.modern_design.banking.application.Deposit;
import jonathan.modern_design.banking.application.GenericUpdateAccount;
import jonathan.modern_design.banking.application.TransferMoney;
import jonathan.modern_design.banking.application.create_account.CreateAccount;
import jonathan.modern_design.banking.domain.policies.AccountNumberGenerator;
import jonathan.modern_design.banking.domain.services.AccountNumberDefaultGenerator;
import jonathan.modern_design.banking.domain.services.AccountValidator;
import jonathan.modern_design.banking.domain.store.AccountHolderRepo;
import jonathan.modern_design.banking.domain.store.AccountHolderRepoInMemory;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import jonathan.modern_design.banking.domain.store.AccountRepoInMemory;
import jonathan.modern_design.banking.domain.store.TransactionRepo;
import jonathan.modern_design.banking.domain.store.TransactionRepoInMemory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AccountingConfig {
    final AccountRepo accountRepo = new AccountRepoInMemory();

    public BankingApi accountApi(
            AccountRepo accountRepo,
            AccountHolderRepo accountHolderRepo,
            TransactionRepo transactionRepo,
            AuthApi userFacade,
            AccountNumberGenerator accountNumberGenerator,
            CountriesCatalog countriesCatalog
    ) {
        AccountValidator accountValidator = new AccountValidator();

        return new BankingApiInternal(
                new TransferMoney(accountRepo, transactionRepo, accountValidator),
                new CreateAccount(accountRepo, accountHolderRepo, userFacade, accountNumberGenerator, countriesCatalog),
                new GenericUpdateAccount(accountRepo),
                new Deposit(accountRepo, transactionRepo)
        );
    }

    @Profile("test")
    public BankingApi accountApi(AuthApi authApi) {
        //For Unit testing
        TransactionRepo transactionRepo = new TransactionRepoInMemory();
        AccountHolderRepo accountHolderRepo = new AccountHolderRepoInMemory();
        CountriesCatalog countriesCatalog = new CountriesCatalogStub();
        AccountNumberGenerator accountNumberGenerator = new AccountNumberDefaultGenerator();

        return accountApi(accountRepo, accountHolderRepo, transactionRepo, authApi, accountNumberGenerator, countriesCatalog);
    }

    @Profile("test")
    public AccountRepo getAccountRepo() {
        //For Unit testing, very useful for midsized apps
        //For very complex, avoid using accountRepo and access instead from the controller -> mockMvc.createAccount vs. accountRepo.createAccount
        return accountRepo;
    }
}
