package com.jonathan.modern_design.account_module;

import com.jonathan.modern_design.account_module.application.AccountFacade;
import com.jonathan.modern_design.account_module.application.create_account.CreateAccountService;
import com.jonathan.modern_design.account_module.application.create_account.CreateAccountUseCase;
import com.jonathan.modern_design.account_module.application.find_account.FindAccountService;
import com.jonathan.modern_design.account_module.application.find_account.FindAccountUseCase;
import com.jonathan.modern_design.account_module.application.send_money.SendMoneyService;
import com.jonathan.modern_design.account_module.application.send_money.SendMoneyUseCase;
import com.jonathan.modern_design.account_module.application.update_account.UpdateAccountService;
import com.jonathan.modern_design.account_module.application.update_account.UpdateAccountUseCase;
import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.services.AccountValidator;
import com.jonathan.modern_design.account_module.infra.AccountController;
import com.jonathan.modern_design.account_module.infra.AccountMapper;
import com.jonathan.modern_design.account_module.infra.AccountMapperAdapter;
import com.jonathan.modern_design.account_module.infra.persistence.AccountRepositorySpringAdapter;
import com.jonathan.modern_design.account_module.infra.persistence.SpringAccountRepository;
import com.jonathan.modern_design.config.annotations.BeanClass;
import com.jonathan.modern_design.config.annotations.DomainService;
import com.jonathan.modern_design.config.annotations.PersistenceAdapter;
import com.jonathan.modern_design.config.annotations.WebAdapter;
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
        },
        excludeFilters = {
                //@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {someExternalAPIStub.class})
        }
)
public class AccountConfiguration {

    @Bean
    public AccountController accountController(AccountFacade accountFacade) {
        return new AccountController(accountFacade);
    }

    @Bean
    public AccountMapper accountMapper() {
        return new AccountMapperAdapter();
    }

    @Bean
    public AccountRepository accountRepository(SpringAccountRepository repository, AccountMapper accountMapper) {
        return new AccountRepositorySpringAdapter(repository, accountMapper);
    }

    @Bean
    public SendMoneyUseCase sendMoneyUseCase(FindAccountUseCase findAccountUseCase, UpdateAccountUseCase updateAccountUseCase) {
        AccountValidator accountValidator = new AccountValidator();

        return new SendMoneyService(findAccountUseCase, updateAccountUseCase, accountValidator);
    }

    @Bean
    public FindAccountUseCase findAccountUseCase(AccountRepository accountRepository) {
        return new FindAccountService(accountRepository);
    }

    @Bean
    public UpdateAccountUseCase updateAccountUseCase(AccountRepository accountRepository) {
        return new UpdateAccountService(accountRepository);
    }

    @Bean
    public CreateAccountUseCase createAccountUseCase(AccountRepository accountRepository, UserFacade userFacade) {
        return new CreateAccountService(accountRepository, userFacade);
    }

    @Bean
    public AccountFacade accountFacade(AccountRepository accountRepository, UserFacade userFacade) {
        UpdateAccountUseCase updateAccountUseCase = updateAccountUseCase(accountRepository);
        SendMoneyUseCase sendMoneyUseCase = sendMoneyUseCase(findAccountUseCase(accountRepository), updateAccountUseCase);
        CreateAccountUseCase createAccountUseCase = createAccountUseCase(accountRepository, userFacade);

        return new AccountFacade(accountRepository, sendMoneyUseCase, updateAccountUseCase, createAccountUseCase);
    }
}
