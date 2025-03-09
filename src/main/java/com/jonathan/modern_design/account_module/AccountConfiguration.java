package com.jonathan.modern_design.account_module;

import com.jonathan.modern_design._infra.config.annotations.BeanClass;
import com.jonathan.modern_design._infra.config.annotations.DomainService;
import com.jonathan.modern_design._infra.config.annotations.PersistenceAdapter;
import com.jonathan.modern_design._infra.config.annotations.WebAdapter;
import com.jonathan.modern_design._shared.country.CountriesInventory;
import com.jonathan.modern_design._shared.country.CountriesInventoryStub;
import com.jonathan.modern_design.account_module.application.CreateAccountUseCase;
import com.jonathan.modern_design.account_module.application.TransferMoneyUseCase;
import com.jonathan.modern_design.account_module.domain.AccountRepo;
import com.jonathan.modern_design.account_module.domain.services.AccountValidator;
import com.jonathan.modern_design.account_module.domain.services.CreateAccountService;
import com.jonathan.modern_design.account_module.domain.services.TransferMoneyService;
import com.jonathan.modern_design.account_module.infra.mapper.AccountMapper;
import com.jonathan.modern_design.account_module.infra.mapper.AccountMapperAdapter;
import com.jonathan.modern_design.account_module.infra.persistence.AccountRepoAdapter;
import com.jonathan.modern_design.account_module.infra.persistence.AccountSpringRepo;
import com.jonathan.modern_design.account_module.infra.query.AccountSearchRepo;
import com.jonathan.modern_design.user_module.UserFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackageClasses = {AccountConfiguration.class},
        includeFilters = {
                //Add Stub class for beta testing on pre-production
                @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {DomainService.class, WebAdapter.class, PersistenceAdapter.class, BeanClass.class})
        }
//        excludeFilters = {
//                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {someExternalAPIStub.class})
//        }
)
public class AccountConfiguration {

    @Bean
    public AccountMapper accountMapper() {
        return new AccountMapperAdapter();
    }

    @Bean
    public AccountRepo accountRepository(AccountSpringRepo repository, AccountMapper accountMapper) {
        return new AccountRepoAdapter(repository, accountMapper);
    }

    @Bean
    public TransferMoneyUseCase transferMoneyUseCase(AccountRepo accountRepo) {
        AccountValidator accountValidator = new AccountValidator();
        return new TransferMoneyService(accountRepo, accountValidator);
    }

    @Bean
    public CreateAccountUseCase createAccountUseCase(AccountRepo accountRepo, UserFacade userFacade, CountriesInventory countriesInventory) {
        return new CreateAccountService(accountRepo, userFacade, countriesInventory);
    }

    @Bean //TODO REMOVE
    public CountriesInventory countriesInventory() {
        return new CountriesInventoryStub();
    }

    @Bean
    public AccountFacade accountFacade(AccountRepo accountRepo, AccountSearchRepo accountSearchRepo, UserFacade userFacade, CountriesInventory countriesInventory) {
        return new AccountFacade(
                accountRepo,
                accountSearchRepo,
                transferMoneyUseCase(accountRepo),
                createAccountUseCase(accountRepo, userFacade, countriesInventory),
                accountMapper()
        );
    }
}
