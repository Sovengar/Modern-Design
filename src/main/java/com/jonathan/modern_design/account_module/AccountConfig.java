package com.jonathan.modern_design.account_module;

import com.jonathan.modern_design._shared.country.CountriesInventory;
import com.jonathan.modern_design._shared.country.CountriesInventoryStub;
import com.jonathan.modern_design.account_module.application.AccountCreator;
import com.jonathan.modern_design.account_module.application.MoneyTransfer;
import com.jonathan.modern_design.account_module.domain.AccountRepo;
import com.jonathan.modern_design.account_module.domain.services.AccountValidator;
import com.jonathan.modern_design.account_module.infra.mapper.AccountMapperAdapter;
import com.jonathan.modern_design.account_module.infra.query.AccountSearchRepo;
import com.jonathan.modern_design.user_module.UserApi;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AccountConfig {
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
    public AccountApi accountApi(AccountRepo accountRepo, UserApi userApi) {
        //Dilemma, accountRepo should be instantiated here, not injected.
        AccountSearchRepo accountSearchRepo = null; //TODO
        final CountriesInventory countriesInventory = new CountriesInventoryStub();

        return accountApi(accountRepo, accountSearchRepo, userApi, countriesInventory);
    }
}
