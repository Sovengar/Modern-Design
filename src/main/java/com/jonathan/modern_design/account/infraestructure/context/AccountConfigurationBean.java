package com.jonathan.modern_design.account.infraestructure.context;

import com.jonathan.modern_design.account.application.AccountFacade;
import com.jonathan.modern_design.account.application.find_account.FindAccountUseCase;
import com.jonathan.modern_design.account.application.send_money.SendMoneyService;
import com.jonathan.modern_design.account.application.send_money.SendMoneyUseCase;
import com.jonathan.modern_design.account.application.update_account.UpdateAccountUseCase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountConfigurationBean {
    @Bean(name = "sendMoneyService")
    SendMoneyService sendMoneyService(@Qualifier("accountPersistenceAdapter") final FindAccountUseCase findAccountUseCase, @Qualifier("accountPersistenceAdapter") final UpdateAccountUseCase updateAccountUseCase) {
        return new SendMoneyService(findAccountUseCase, updateAccountUseCase);
    }

    @Bean(name = "accountServiceFacade")
    AccountFacade AccountServiceFacade(final FindAccountUseCase findAccountUseCase, final UpdateAccountUseCase updateAccountUseCase, final SendMoneyUseCase sendMoneyUseCase) {
        return new AccountFacade(sendMoneyUseCase, findAccountUseCase, updateAccountUseCase);
    }
}
