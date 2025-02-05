package com.jonathan.modern_design.account_module;

import com.jonathan.modern_design.account_module.application.AccountFacade;
import com.jonathan.modern_design.account_module.application.AccountMapper;
import com.jonathan.modern_design.account_module.application.AccountMapperAdapter;
import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.infraestructure.context.AccountConfigurationFactory;
import com.jonathan.modern_design.account_module.infraestructure.persistence.AccountRepositorySpringAdapter;
import com.jonathan.modern_design.account_module.infraestructure.persistence.SpringAccountRepository;
import com.jonathan.modern_design.config.RepositoryIntegrationTestConfig;
import com.jonathan.modern_design.fake_data.AccountStub;
import com.jonathan.modern_design.user_module.UserFacade;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static com.jonathan.modern_design.fake_data.SendMoneyMother.transactionWithAmount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Import(SendMoneyRepositoryIT.TestConfig.class)
class SendMoneyRepositoryIT extends RepositoryIntegrationTestConfig {

    @Mock
    private UserFacade userFacade;

    @Autowired
    private AccountRepository repository;

    private AccountFacade accountFacade;

    @Autowired
    public SendMoneyRepositoryIT(AccountRepository repository) {
        this.repository = repository;
        this.accountFacade = new AccountConfigurationFactory().accountFacade(repository, userFacade);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public AccountRepository accountRepository(SpringAccountRepository repository) {
            return new AccountRepositorySpringAdapter(repository, new AccountMapperAdapter());
        }
    }

    private void poblatePersistenceLayer(Account source, Account target) {
        repository.create(source);
        repository.create(target);
    }

    @Test
    void should_send_money_into_the_target_account() {
        Account source = AccountStub.sourceAccountwithBalance(100.0);
        Account target = AccountStub.targetAccountEmpty();
        poblatePersistenceLayer(source, target);

        accountFacade.sendMoney(transactionWithAmount(50.0));

        assertThat(target.getAmount()).isEqualTo(BigDecimal.valueOf(50.0));
    }
}
