package com.jonathan.modern_design.account_module;

import com.jonathan.modern_design.__config.RepositoryITConfig;
import com.jonathan.modern_design._fake_data.CreateAccountStub;
import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.account_module.application.AccountFacade;
import com.jonathan.modern_design.account_module.application.deposit.DepositCommand;
import com.jonathan.modern_design.account_module.infra.persistence.AccountPersistenceAdapter;
import com.jonathan.modern_design.user_module.application.RegisterUserCommand;
import com.jonathan.modern_design.user_module.application.UserFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static com.jonathan.modern_design._fake_data.SendMoneyMother.fromAccountToAccountWithAmount;
import static com.jonathan.modern_design._fake_data.UserStub.normalUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Import(AccountConfiguration.class)
class TransferMoneyRepositoryIT extends RepositoryITConfig {

    @Autowired
    private AccountPersistenceAdapter repository;

    @Autowired
    private AccountFacade accountFacade;

    @MockBean
    private UserFacade userFacade;

    @Test
    void should_send_money_into_the_target_account() {
        var source = accountFacade.createAccount(CreateAccountStub.randomAccountWithCurrency(Currency.EURO));
        source = accountFacade.deposit(new DepositCommand(source.getAccountNumber(), BigDecimal.valueOf(100), Currency.EURO));
        var target = accountFacade.createAccount(CreateAccountStub.randomAccountWithCurrency(Currency.EURO));
        when(userFacade.registerUser(any(RegisterUserCommand.class))).thenReturn(normalUser()); //TODO DEVUELVE NULL

        accountFacade.transferMoney(fromAccountToAccountWithAmount(source.getAccountNumber(), target.getAccountNumber(), 60.0));

        source = repository.findOne(source.getAccountNumber()).orElseThrow();
        target = repository.findOne(target.getAccountNumber()).orElseThrow();

        assertThat(source.getMoney().getBalance()).isEqualTo(BigDecimal.valueOf(40.0));
        assertThat(target.getMoney().getBalance()).isEqualTo(BigDecimal.valueOf(60.0));
    }
}
