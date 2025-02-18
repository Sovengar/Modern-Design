package com.jonathan.modern_design.account_module;

import com.jonathan.modern_design.account_module.application.AccountFacade;
import com.jonathan.modern_design.account_module.application.create_account.CreateAccountService;
import com.jonathan.modern_design.account_module.application.create_account.CreateAccountUseCase;
import com.jonathan.modern_design.account_module.application.deposit.DepositService;
import com.jonathan.modern_design.account_module.application.deposit.DepositUseCase;
import com.jonathan.modern_design.account_module.application.find_account.FindAccountService;
import com.jonathan.modern_design.account_module.application.find_account.FindAccountUseCase;
import com.jonathan.modern_design.account_module.application.transfer_money.TransferMoneyService;
import com.jonathan.modern_design.account_module.application.transfer_money.TransferMoneyUseCase;
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
    public TransferMoneyUseCase sendMoneyUseCase(FindAccountUseCase findAccountUseCase, UpdateAccountUseCase updateAccountUseCase) {
        AccountValidator accountValidator = new AccountValidator();

        return new TransferMoneyService(findAccountUseCase, updateAccountUseCase, accountValidator);
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
    public DepositUseCase depositUseCase(AccountRepository accountRepository) {
        return new DepositService(accountRepository);
    }

    @Bean
    public AccountFacade accountFacade(AccountRepository accountRepository, UserFacade userFacade) {
        UpdateAccountUseCase updateAccountUseCase = updateAccountUseCase(accountRepository);
        TransferMoneyUseCase transferMoneyUseCase = sendMoneyUseCase(findAccountUseCase(accountRepository), updateAccountUseCase);
        CreateAccountUseCase createAccountUseCase = createAccountUseCase(accountRepository, userFacade);
        DepositUseCase depositUseCase = depositUseCase(accountRepository);

        return new AccountFacade(accountRepository, transferMoneyUseCase, updateAccountUseCase, createAccountUseCase, depositUseCase);
    }
}
