package jonathan.modern_design.account_module;

import jonathan.modern_design.__config.PrettyTestNames;
import jonathan.modern_design.__config.TimeExtension;
import jonathan.modern_design.account_module.api.AccountApi;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.exceptions.AccountIsInactiveException;
import jonathan.modern_design.account_module.domain.exceptions.OperationWithDifferentCurrenciesException;
import jonathan.modern_design.account_module.domain.vo.AccountMoney;
import jonathan.modern_design.account_module.infra.AccountingConfig;
import jonathan.modern_design.user.api.UserApi;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static jonathan.modern_design._fake_data.AccountStub.AccountMother.sourceAccountEmpty;
import static jonathan.modern_design._fake_data.AccountStub.AccountMother.sourceAccountInactive;
import static jonathan.modern_design._fake_data.AccountStub.AccountMother.sourceAccountWithBalance;
import static jonathan.modern_design._fake_data.AccountStub.AccountMother.targetAccountEmpty;
import static jonathan.modern_design._fake_data.AccountStub.AccountMother.targetAccountInactive;
import static jonathan.modern_design._fake_data.AccountStub.AccountMother.targetAccountWithBalance;
import static jonathan.modern_design._fake_data.AccountStub.AccountMother.targetAccountWithDifferentCurrency;
import static jonathan.modern_design._fake_data.AccountStub.TransferMoneyMother.transactionWithAmount;
import static jonathan.modern_design._shared.Currency.EUR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayNameGeneration(PrettyTestNames.class)
class TransferMoneyTest {
    private final LocalDateTime supposedToBeNow = LocalDate.of(2020, 12, 25).atStartOfDay();
    private final AccountingConfig accountingConfig = new AccountingConfig();
    @RegisterExtension
    TimeExtension timeExtension = new TimeExtension(supposedToBeNow);
    @MockitoBean
    private UserApi userApi;
    private final AccountApi accountFacade = accountingConfig.accountApi(userApi);

    private void populatePersistenceLayer(Account source, Account target) {
        final var accountRepo = accountingConfig.getAccountRepo();
        accountRepo.create(source);
        accountRepo.create(target);
    }

    @Nested
    class WithValidAccountShould {
        @Test
        void transfer_money_into_the_target_account() {
            var source = sourceAccountWithBalance(100.0);
            var target = targetAccountEmpty();
            populatePersistenceLayer(source, target);

            accountFacade.transferMoney(transactionWithAmount(AccountMoney.of(BigDecimal.valueOf(50.0), EUR)));

            assertThat(target.money().amount()).isEqualTo(BigDecimal.valueOf(50.0));
        }

        @Test
        void update_date_of_last_transaction() {
            var source = sourceAccountWithBalance(100.0);
            var target = targetAccountEmpty();
            populatePersistenceLayer(source, target);

            accountFacade.transferMoney(transactionWithAmount(AccountMoney.of(BigDecimal.valueOf(50.0), EUR)));

            assertThat(target.dateOfLastTransaction()).isEqualTo(supposedToBeNow);
        }
    }

    @Nested
    class WithInvalidAccountShouldFailIf {
        @Test
        void transference_with_insufficient_money() {
            var source = sourceAccountEmpty();
            var target = targetAccountWithBalance(100.0);
            populatePersistenceLayer(source, target);

            assertThatThrownBy(() -> accountFacade.transferMoney(transactionWithAmount(AccountMoney.of(BigDecimal.valueOf(50.0), EUR))))
                    .isInstanceOf(AccountMoney.InsufficientFundsException.class);
        }

        @Test
        void transference_with_inactive_source_account() {
            var source = sourceAccountInactive();
            var target = targetAccountEmpty();
            populatePersistenceLayer(source, target);

            assertThatThrownBy(() -> accountFacade.transferMoney(transactionWithAmount(AccountMoney.of(BigDecimal.valueOf(50.0), EUR))))
                    .isInstanceOf(AccountIsInactiveException.class);
        }

        @Test
        void transference_with_inactive_target_account() {
            var source = sourceAccountWithBalance(100.0);
            var target = targetAccountInactive();
            populatePersistenceLayer(source, target);

            assertThatThrownBy(() -> accountFacade.transferMoney(transactionWithAmount(AccountMoney.of(BigDecimal.valueOf(50.0), EUR))))
                    .isInstanceOf(AccountIsInactiveException.class);
        }

        @Test
        void accounts_have_distinct_currencies() {
            var source = sourceAccountWithBalance(100.0);
            var target = targetAccountWithDifferentCurrency();
            populatePersistenceLayer(source, target);

            assertThatThrownBy(() -> accountFacade.transferMoney(transactionWithAmount(AccountMoney.of(BigDecimal.valueOf(50.0), EUR))))
                    .isInstanceOf(OperationWithDifferentCurrenciesException.class);
        }
    }
}
