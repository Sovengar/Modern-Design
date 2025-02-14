package com.jonathan.modern_design.account_module;

import com.jonathan.modern_design.__config.RepositoryITConfig;
import com.jonathan.modern_design._fake_data.AccountStub;
import com.jonathan.modern_design.account_module.application.AccountFacade;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.infra.persistence.AccountRepositorySpringAdapter;
import com.jonathan.modern_design.user_module.UserFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static com.jonathan.modern_design._fake_data.SendMoneyMother.transactionWithAmount;
import static org.assertj.core.api.Assertions.assertThat;

@Import(AccountConfiguration.class)
class SendMoneyRepositoryIT extends RepositoryITConfig {

    @Autowired
    private AccountRepositorySpringAdapter repository;

    @Autowired
    private AccountFacade accountFacade;

    @MockBean
    private UserFacade userFacade;

    @Test
    void should_send_money_into_the_target_account() {
        Account source = repository.create(AccountStub.sourceAccountwithBalance(100.0));
        Account target = repository.create(AccountStub.targetAccountEmpty());

        accountFacade.sendMoney(transactionWithAmount(60.0));

        source = repository.findOne(source.getAccountNumber()).orElseThrow();
        target = repository.findOne(target.getAccountNumber()).orElseThrow();

        assertThat(source.getAmount()).isEqualTo(BigDecimal.valueOf(40.0));
        assertThat(target.getAmount()).isEqualTo(BigDecimal.valueOf(60.0));
    }
}
