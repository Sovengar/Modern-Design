package com.jonathan.modern_design.account_module.infra;

import com.jonathan.modern_design._shared.country.CountriesInventory;
import com.jonathan.modern_design._shared.country.CountriesInventoryStub;
import com.jonathan.modern_design.account_module.AccountApi;
import com.jonathan.modern_design.account_module.application.AccountCreator;
import com.jonathan.modern_design.account_module.application.MoneyTransfer;
import com.jonathan.modern_design.account_module.domain.AccountRepo;
import com.jonathan.modern_design.account_module.domain.services.AccountValidator;
import com.jonathan.modern_design.account_module.infra.query.AccountSearchRepo;
import com.jonathan.modern_design.user_module.UserApi;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AccountConfig {
    final AccountRepo accountRepo = new AccountInMemoryRepo();

    public AccountApi accountApi(AccountRepo accountRepo, AccountSearchRepo accountSearchRepo, UserApi userFacade, CountriesInventory countriesInventory) {
        AccountValidator accountValidator = new AccountValidator();

        return new AccountFacade(
                accountRepo,
                accountSearchRepo,
                new MoneyTransfer(accountRepo, accountValidator),
                new AccountCreator(accountRepo, userFacade, countriesInventory),
                new AccountMapperAdapter()
        );
    }

    @Profile("test")
    public AccountApi accountApi(UserApi userApi) {
        //For Unit testing
        EntityManager entityManager = null;
        AccountSearchRepo accountSearchRepo = new AccountSearchRepo(entityManager);
        final CountriesInventory countriesInventory = new CountriesInventoryStub();

        return accountApi(accountRepo, accountSearchRepo, userApi, countriesInventory);
    }

    @Profile("test")
    public AccountRepo getAccountRepo() {
        //For Unit testing
        return accountRepo;
    }
}
