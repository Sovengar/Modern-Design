package com.jonathan.modern_design.account_module.infraestructure.context;

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
import com.jonathan.modern_design.account_module.infraestructure.persistence.AccountRepositorySpringAdapter;
import com.jonathan.modern_design.account_module.infraestructure.persistence.SpringAccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountConfigurationFactory {

    @Bean(name = "accountRepositorySpringAdapter")
    public AccountRepository accountRepository(SpringAccountRepository springAccountRepository) {
        return new AccountRepositorySpringAdapter(springAccountRepository);
    }

    @Bean(name = "accountFacade")
    public AccountFacade accountFacade(AccountRepository accountRepository) {
        UpdateAccountUseCase updateAccountUseCase = new UpdateAccountService(accountRepository);
        FindAccountUseCase findAccountUseCase = new FindAccountService(accountRepository);

        AccountValidator accountValidator = new AccountValidator();

        SendMoneyUseCase sendMoneyUseCase = new SendMoneyService(findAccountUseCase, updateAccountUseCase, accountValidator);
        CreateAccountUseCase createAccountUseCase = new CreateAccountService(accountRepository);

        return new AccountFacade(accountRepository, sendMoneyUseCase, updateAccountUseCase, createAccountUseCase);
    }
}
