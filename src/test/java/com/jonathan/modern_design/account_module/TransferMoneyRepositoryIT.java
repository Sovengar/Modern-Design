package com.jonathan.modern_design.account_module;

import com.jonathan.modern_design.__config.RepositoryITConfig;
import com.jonathan.modern_design._shared.country.CountriesInventory;
import com.jonathan.modern_design._shared.country.CountriesInventoryStub;
import com.jonathan.modern_design.account_module.domain.AccountRepo;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountMoney;
import com.jonathan.modern_design.account_module.dtos.DepositCommand;
import com.jonathan.modern_design.account_module.infra.mapper.AccountMapper;
import com.jonathan.modern_design.account_module.infra.mapper.AccountMapperAdapter;
import com.jonathan.modern_design.account_module.infra.persistence.AccountRepoAdapter;
import com.jonathan.modern_design.account_module.infra.persistence.AccountSpringRepo;
import com.jonathan.modern_design.account_module.infra.query.AccountSearchRepo;
import com.jonathan.modern_design.user_module.UserApi;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static com.jonathan.modern_design._fake_data.AccountStub.CreateAccountMother.randomAccountWithCurrency;
import static com.jonathan.modern_design._fake_data.AccountStub.TransferMoneyMother.fromAccountToAccountWithAmount;
import static com.jonathan.modern_design._shared.Currency.EURO;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;

//I would like to avoid TestConfig, it """"works"""" with @MockBean here + @Import(AccountConfig.class)
@Import(TransferMoneyRepositoryIT.TestConfig.class)
class TransferMoneyRepositoryIT extends RepositoryITConfig {
    @Autowired
    private AccountApi accountFacade;

    @Autowired
    private AccountRepo repository;

    //when(userFacade.registerUser(any())).thenReturn(normalUser().getUuid());

    @Test
    void should_send_money_into_the_target_account() {
        var source = getAccountWithMoney(AccountMoney.of(BigDecimal.valueOf(100.0), EURO));
        var target = getAccountWithMoney(AccountMoney.of(ZERO, EURO));
        accountFacade.transferMoney(fromAccountToAccountWithAmount(source.getAccountNumber().getValue(), target.getAccountNumber().getValue(), AccountMoney.of(BigDecimal.valueOf(60.0), EURO)));

        assertThat(source.getMoney().getAmount()).isEqualTo(BigDecimal.valueOf(40.0));
        assertThat(target.getMoney().getAmount()).isEqualTo(BigDecimal.valueOf(60.0));
    }

    private Account getAccountWithMoney(final AccountMoney money) {
        var accountNumber = accountFacade.createAccount(randomAccountWithCurrency(money.getCurrency())).getValue();

        if (money.isPositive()) {
            accountFacade.deposit(new DepositCommand(accountNumber, money.getAmount(), money.getCurrency()));
        }

        return repository.findOne(accountNumber).orElseThrow();
    }

    @TestConfiguration
    static class TestConfig {

        @MockBean
        private UserApi userFacade;

        @Bean
        public AccountMapper accountMapper() {
            return new AccountMapperAdapter();
        }

        @Bean
        public AccountRepo accountRepository(AccountSpringRepo repository, AccountMapper accountMapper) {
            return new AccountRepoAdapter(repository, accountMapper);
        }

        @Bean
        public AccountSearchRepo accountSearchRepo(EntityManager entityManager) {
            return new AccountSearchRepo(entityManager);
        }

        @Bean
        public AccountApi accountFacade(AccountRepoAdapter repository, AccountSearchRepo repoSearch) {
            CountriesInventory countriesInventory = new CountriesInventoryStub();
            return new AccountConfiguration().accountFacade(repository, repoSearch, userFacade, countriesInventory);
        }
    }
}
