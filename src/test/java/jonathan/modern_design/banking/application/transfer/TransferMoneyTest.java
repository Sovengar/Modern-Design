package jonathan.modern_design.banking.application.transfer;

import jonathan.modern_design.__config.TimeExtension;
import jonathan.modern_design.__config.shared_for_all_classes.UnitTest;
import jonathan.modern_design._shared.exceptions.OperationWithDifferentCurrenciesException;
import jonathan.modern_design._shared.vo.Money;
import jonathan.modern_design.auth.api.AuthApi;
import jonathan.modern_design.banking.api.BankingApi;
import jonathan.modern_design.banking.domain.exceptions.AccountIsInactiveException;
import jonathan.modern_design.banking.domain.models.Account;
import jonathan.modern_design.banking.infra.AccountingConfig;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static jonathan.modern_design._dsl.AccountStub.AccountMother.sourceAccountEmpty;
import static jonathan.modern_design._dsl.AccountStub.AccountMother.sourceAccountInactive;
import static jonathan.modern_design._dsl.AccountStub.AccountMother.sourceAccountWithBalance;
import static jonathan.modern_design._dsl.AccountStub.AccountMother.targetAccountEmpty;
import static jonathan.modern_design._dsl.AccountStub.AccountMother.targetAccountInactive;
import static jonathan.modern_design._dsl.AccountStub.AccountMother.targetAccountWithBalance;
import static jonathan.modern_design._dsl.AccountStub.AccountMother.targetAccountWithDifferentCurrency;
import static jonathan.modern_design._dsl.AccountStub.TransferMoneyMother.transactionWithAmount;
import static jonathan.modern_design._shared.Currency.EUR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@UnitTest
class TransferMoneyTest {
    private final LocalDateTime supposedToBeNow = LocalDate.of(2020, 12, 25).atStartOfDay();
    private final AccountingConfig accountingConfig = new AccountingConfig();
    @RegisterExtension
    TimeExtension timeExtension = new TimeExtension(supposedToBeNow);
    @MockitoBean
    private AuthApi authApi;
    private final BankingApi bankingApi = accountingConfig.accountApi(authApi);

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

            bankingApi.transferMoney(transactionWithAmount(Money.of(BigDecimal.valueOf(50.0), EUR)));

            assertThat(target.getMoney().getBalance()).isEqualTo(BigDecimal.valueOf(50.0));
        }
    }

    @Nested
    class WithInvalidAccountShouldFailIf {
        @Test
        void transference_with_insufficient_money() {
            var source = sourceAccountEmpty();
            var target = targetAccountWithBalance(100.0);
            populatePersistenceLayer(source, target);

            assertThatThrownBy(() -> bankingApi.transferMoney(transactionWithAmount(Money.of(BigDecimal.valueOf(50.0), EUR))))
                    .isInstanceOf(Money.InsufficientFundsException.class);
        }

        @Test
        void transference_with_inactive_source_account() {
            var source = sourceAccountInactive();
            var target = targetAccountEmpty();
            populatePersistenceLayer(source, target);

            assertThatThrownBy(() -> bankingApi.transferMoney(transactionWithAmount(Money.of(BigDecimal.valueOf(50.0), EUR))))
                    .isInstanceOf(AccountIsInactiveException.class);
        }

        @Test
        void transference_with_inactive_target_account() {
            var source = sourceAccountWithBalance(100.0);
            var target = targetAccountInactive();
            populatePersistenceLayer(source, target);

            assertThatThrownBy(() -> bankingApi.transferMoney(transactionWithAmount(Money.of(BigDecimal.valueOf(50.0), EUR))))
                    .isInstanceOf(AccountIsInactiveException.class);
        }

        @Test
        void accounts_have_distinct_currencies() {
            var source = sourceAccountWithBalance(100.0);
            var target = targetAccountWithDifferentCurrency();
            populatePersistenceLayer(source, target);

            assertThatThrownBy(() -> bankingApi.transferMoney(transactionWithAmount(Money.of(BigDecimal.valueOf(50.0), EUR))))
                    .isInstanceOf(OperationWithDifferentCurrenciesException.class);
        }
    }
}
