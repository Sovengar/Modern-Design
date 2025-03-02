package com.jonathan.modern_design.account_module;

import com.jonathan.modern_design.__config.PrettyTestNames;
import com.jonathan.modern_design.__config.TimeExtension;
import com.jonathan.modern_design._fake_data.AccountStub;
import com.jonathan.modern_design._shared.country.CountriesInventory;
import com.jonathan.modern_design._shared.country.CountriesInventoryStub;
import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.exceptions.AccountIsInactiveException;
import com.jonathan.modern_design.account_module.domain.exceptions.OperationWithDifferentCurrenciesException;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountMoney;
import com.jonathan.modern_design.account_module.infra.persistence.InMemoryAccountRepository;
import com.jonathan.modern_design.user_module.UserFacade;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.jonathan.modern_design._fake_data.AccountStub.TransferMoneyMother.transactionWithAmount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayNameGeneration(PrettyTestNames.class)
class TransferMoneyTest {
    private final AccountRepository repository = new InMemoryAccountRepository();
    private final LocalDateTime supposedToBeNow = LocalDate.of(2020, 12, 25).atStartOfDay();
    private final CountriesInventory countriesInventory = new CountriesInventoryStub();
    @RegisterExtension
    TimeExtension timeExtension = new TimeExtension(supposedToBeNow);
    @Mock
    private UserFacade userFacade;
    private final AccountFacade accountFacade = new AccountConfiguration().accountFacade(repository, userFacade, countriesInventory);

    private void poblatePersistenceLayer(Account source, Account target) {
        repository.create(source);
        repository.create(target);
    }

    @Nested
    class ValidAccount {
        @Test
        void should_transfer_money_into_the_target_account() {
            Account source = AccountStub.sourceAccountwithBalance(100.0);
            Account target = AccountStub.targetAccountEmpty();
            poblatePersistenceLayer(source, target);

            accountFacade.transferMoney(transactionWithAmount(50.0));

            assertThat(target.getMoney().getAmount()).isEqualTo(BigDecimal.valueOf(50.0));
        }

        @Test
        void should_update_date_of_last_transaction() {
            Account source = AccountStub.sourceAccountwithBalance(100.0);
            Account target = AccountStub.targetAccountEmpty();
            poblatePersistenceLayer(source, target);

            accountFacade.transferMoney(transactionWithAmount(50.0));

            assertThat(target.getDateOfLastTransaction()).isEqualTo(supposedToBeNow);
        }
    }

    @Nested
    class InvalidAccounts {
        @Test
        void should_fail_transference_when_not_enough_money() {
            Account source = AccountStub.sourceAccountEmpty();
            Account target = AccountStub.targetAccountwithBalance(100.0);
            poblatePersistenceLayer(source, target);

            assertThatThrownBy(() -> accountFacade.transferMoney(transactionWithAmount(50.0)))
                    .isInstanceOf(AccountMoney.InsufficientFundsException.class);
        }

        @Test
        void should_fail_transference_when_source_account_is_inactive() {
            Account source = AccountStub.sourceAccountInactive();
            Account target = AccountStub.targetAccountEmpty();
            poblatePersistenceLayer(source, target);

            assertThatThrownBy(() -> accountFacade.transferMoney(transactionWithAmount(50.0)))
                    .isInstanceOf(AccountIsInactiveException.class);
        }

        @Test
        void should_fail_transference_when_target_account_is_inactive() {
            Account source = AccountStub.sourceAccountwithBalance(100.0);
            Account target = AccountStub.targetAccountInactive();
            poblatePersistenceLayer(source, target);

            assertThatThrownBy(() -> accountFacade.transferMoney(transactionWithAmount(50.0)))
                    .isInstanceOf(AccountIsInactiveException.class);
        }

        @Test
        void should_fail_when_accounts_have_distinct_currencies() {
            Account source = AccountStub.sourceAccountwithBalance(100.0);
            Account target = AccountStub.targetAccountWithDifferentCurrency();
            poblatePersistenceLayer(source, target);

            assertThatThrownBy(() -> accountFacade.transferMoney(transactionWithAmount(50.0)))
                    .isInstanceOf(OperationWithDifferentCurrenciesException.class);
        }
    }
}
