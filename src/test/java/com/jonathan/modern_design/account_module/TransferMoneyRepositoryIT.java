package com.jonathan.modern_design.account_module;

import com.jonathan.modern_design.__config.RepositoryITConfig;
import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.account_module.application.DepositUseCase;
import com.jonathan.modern_design.account_module.infra.persistence.AccountPersistenceAdapter;
import com.jonathan.modern_design.user_module.UserFacade;
import com.jonathan.modern_design.user_module.application.RegisterUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static com.jonathan.modern_design._fake_data.AccountStub.CreateAccountMother.randomAccountWithCurrency;
import static com.jonathan.modern_design._fake_data.AccountStub.TransferMoneyMother.fromAccountToAccountWithAmount;
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
        var source = accountFacade.createAccount(randomAccountWithCurrency(Currency.EURO));
        var sourceNumber = source.getAccountNumber().getValue();
        accountFacade.deposit(new DepositUseCase.DepositCommand(sourceNumber, BigDecimal.valueOf(100), Currency.EURO));
        var target = accountFacade.createAccount(randomAccountWithCurrency(Currency.EURO));
        when(userFacade.registerUser(any(RegisterUserUseCase.RegisterUserCommand.class))).thenReturn(normalUser()); //TODO DEVUELVE NULL

        var targetNumber = target.getAccountNumber().getValue();
        accountFacade.transferMoney(fromAccountToAccountWithAmount(sourceNumber, targetNumber, 60.0));

        source = repository.findOne(sourceNumber).orElseThrow();
        target = repository.findOne(targetNumber).orElseThrow();

        assertThat(source.getMoney().getAmount()).isEqualTo(BigDecimal.valueOf(40.0));
        assertThat(target.getMoney().getAmount()).isEqualTo(BigDecimal.valueOf(60.0));
    }
}
