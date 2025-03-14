package com.jonathan.modern_design.account_module;

import com.jonathan.modern_design.__config.PrettyTestNames;
import com.jonathan.modern_design.__config.TimeExtension;
import com.jonathan.modern_design.account_module.domain.AccountRepo;
import com.jonathan.modern_design.account_module.domain.exceptions.AccountIsInactiveException;
import com.jonathan.modern_design.account_module.domain.exceptions.OperationWithDifferentCurrenciesException;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountMoney;
import com.jonathan.modern_design.account_module.infra.persistence.AccountInMemoryRepo;
import com.jonathan.modern_design.user_module.UserApi;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.jonathan.modern_design._fake_data.AccountStub.AccountMother.sourceAccountEmpty;
import static com.jonathan.modern_design._fake_data.AccountStub.AccountMother.sourceAccountInactive;
import static com.jonathan.modern_design._fake_data.AccountStub.AccountMother.sourceAccountWithBalance;
import static com.jonathan.modern_design._fake_data.AccountStub.AccountMother.targetAccountEmpty;
import static com.jonathan.modern_design._fake_data.AccountStub.AccountMother.targetAccountInactive;
import static com.jonathan.modern_design._fake_data.AccountStub.AccountMother.targetAccountWithBalance;
import static com.jonathan.modern_design._fake_data.AccountStub.AccountMother.targetAccountWithDifferentCurrency;
import static com.jonathan.modern_design._fake_data.AccountStub.TransferMoneyMother.transactionWithAmount;
import static com.jonathan.modern_design._shared.Currency.EURO;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayNameGeneration(PrettyTestNames.class)
class TransferMoneyTest {
    private final LocalDateTime supposedToBeNow = LocalDate.of(2020, 12, 25).atStartOfDay();
    private final AccountRepo accountRepo = new AccountInMemoryRepo();
    @RegisterExtension
    TimeExtension timeExtension = new TimeExtension(supposedToBeNow);
    @MockitoBean
    private UserApi userApi;
    private final AccountApi accountFacade = new AccountConfig().accountApi(accountRepo, userApi);

    private void populatePersistenceLayer(Account source, Account target) {
        accountRepo.create(source);
        accountRepo.create(target);
    }

    @Nested
    class ValidAccount {
        @Test
        void should_transfer_money_into_the_target_account() {
            var source = sourceAccountWithBalance(100.0);
            var target = targetAccountEmpty();
            populatePersistenceLayer(source, target);

            accountFacade.transferMoney(transactionWithAmount(AccountMoney.of(BigDecimal.valueOf(50.0), EURO)));

            assertThat(target.getMoney().getAmount()).isEqualTo(BigDecimal.valueOf(50.0));
        }

        @Test
        void should_update_date_of_last_transaction() {
            var source = sourceAccountWithBalance(100.0);
            var target = targetAccountEmpty();
            populatePersistenceLayer(source, target);

            accountFacade.transferMoney(transactionWithAmount(AccountMoney.of(BigDecimal.valueOf(50.0), EURO)));

            assertThat(target.getDateOfLastTransaction()).isEqualTo(supposedToBeNow);
        }
    }

    @Nested
    class InvalidAccounts {
        @Test
        void should_fail_transference_when_not_enough_money() {
            var source = sourceAccountEmpty();
            var target = targetAccountWithBalance(100.0);
            populatePersistenceLayer(source, target);

            assertThatThrownBy(() -> accountFacade.transferMoney(transactionWithAmount(AccountMoney.of(BigDecimal.valueOf(50.0), EURO))))
                    .isInstanceOf(AccountMoney.InsufficientFundsException.class);
        }

        @Test
        void should_fail_transference_when_source_account_is_inactive() {
            var source = sourceAccountInactive();
            var target = targetAccountEmpty();
            populatePersistenceLayer(source, target);

            assertThatThrownBy(() -> accountFacade.transferMoney(transactionWithAmount(AccountMoney.of(BigDecimal.valueOf(50.0), EURO))))
                    .isInstanceOf(AccountIsInactiveException.class);
        }

        @Test
        void should_fail_transference_when_target_account_is_inactive() {
            var source = sourceAccountWithBalance(100.0);
            var target = targetAccountInactive();
            populatePersistenceLayer(source, target);

            assertThatThrownBy(() -> accountFacade.transferMoney(transactionWithAmount(AccountMoney.of(BigDecimal.valueOf(50.0), EURO))))
                    .isInstanceOf(AccountIsInactiveException.class);
        }

        @Test
        void should_fail_when_accounts_have_distinct_currencies() {
            var source = sourceAccountWithBalance(100.0);
            var target = targetAccountWithDifferentCurrency();
            populatePersistenceLayer(source, target);

            assertThatThrownBy(() -> accountFacade.transferMoney(transactionWithAmount(AccountMoney.of(BigDecimal.valueOf(50.0), EURO))))
                    .isInstanceOf(OperationWithDifferentCurrenciesException.class);
        }
    }
}
