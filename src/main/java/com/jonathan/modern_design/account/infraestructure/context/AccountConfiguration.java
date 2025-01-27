package com.jonathan.modern_design.account.infraestructure.context;

import com.jonathan.modern_design.account.application.AccountFacade;
import com.jonathan.modern_design.account.application.find_account.FindAccountService;
import com.jonathan.modern_design.account.application.find_account.FindAccountUseCase;
import com.jonathan.modern_design.account.application.send_money.SendMoneyService;
import com.jonathan.modern_design.account.application.send_money.SendMoneyUseCase;
import com.jonathan.modern_design.account.application.update_account.UpdateAccountService;
import com.jonathan.modern_design.account.application.update_account.UpdateAccountUseCase;
import com.jonathan.modern_design.account.infraestructure.persistence.AccountRepository;
import com.jonathan.modern_design.account.infraestructure.persistence.AccountRepositorySpringAdapter;
import com.jonathan.modern_design.account.infraestructure.persistence.InMemoryAccountRepository;
import com.jonathan.modern_design.account.infraestructure.persistence.SpringAccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountConfiguration {

    //For testing
    public AccountFacade accountFacade() {
        return accountFacade(new InMemoryAccountRepository());
    }

    //For Production
    @Bean(name = "accountRepositorySpringAdapter")
    public AccountRepository accountRepository(SpringAccountRepository springAccountRepository) {
        return new AccountRepositorySpringAdapter(springAccountRepository);
    }

    @Bean(name = "accountFacade")
    AccountFacade accountFacade(AccountRepository accountRepository) {
        UpdateAccountUseCase updateAccountUseCase = new UpdateAccountService(accountRepository);
        FindAccountUseCase findAccountUseCase = new FindAccountService(accountRepository);

        SendMoneyUseCase sendMoneyUseCase = new SendMoneyService(findAccountUseCase, updateAccountUseCase);

        return new AccountFacade(accountRepository, sendMoneyUseCase, updateAccountUseCase);
    }
}
