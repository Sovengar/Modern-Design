package jonathan.modern_design.banking.application.transfer;

import jonathan.modern_design._shared.domain.exceptions.OperationWithDifferentCurrenciesException;
import jonathan.modern_design._shared.domain.vo.Money;
import jonathan.modern_design._utils.TimeExtension;
import jonathan.modern_design.banking.BankingUnitConfig;
import jonathan.modern_design.banking.domain.exceptions.AccountIsInactiveException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static jonathan.modern_design._shared.domain.catalogs.Currency.EUR;
import static jonathan.modern_design.banking.application.transfer.TransferMoneyDsl.transactionWithAmount;
import static jonathan.modern_design.banking.domain.AccountDsl.DEFAULT_TARGET_ACCOUNT_NUMBER;
import static jonathan.modern_design.banking.domain.AccountDsl.emptyTargetAccount;
import static jonathan.modern_design.banking.domain.AccountDsl.givenAnAccountWithBalance;
import static jonathan.modern_design.banking.domain.AccountDsl.givenAnEmptyAccount;
import static jonathan.modern_design.banking.domain.AccountDsl.givenAnInactiveAccount;
import static jonathan.modern_design.banking.domain.AccountDsl.givenAntargetAccountWithDifferentCurrency;
import static jonathan.modern_design.banking.domain.AccountDsl.inactiveTargetAccount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class TransferMoneyTest extends BankingUnitConfig {
    private final LocalDateTime supposedToBeNow = LocalDate.of(2020, 12, 25).atStartOfDay();
    @RegisterExtension
    TimeExtension timeExtension = new TimeExtension(supposedToBeNow);

    @Nested
    class WithValidAccountShould {
        @Test
        void transfer_money_into_the_target_account() {
            var source = givenAnAccountWithBalance(100.0);
            var target = emptyTargetAccount();
            accountRepo.create(source);
            accountRepo.create(target);

            bankingApi.transferMoney(transactionWithAmount(Money.of(50.0, EUR)));

            assertThat(target.getMoney().getBalance()).isEqualTo(BigDecimal.valueOf(50.0));
        }
    }

    @Nested
    class WithInvalidAccountShouldFailIf {
        @Test
        void transference_with_insufficient_money() {
            var source = givenAnEmptyAccount();
            var target = givenAnAccountWithBalance(100.0, DEFAULT_TARGET_ACCOUNT_NUMBER);
            accountRepo.create(source);
            accountRepo.create(target);

            assertThatThrownBy(() -> bankingApi.transferMoney(transactionWithAmount(Money.of(50.0, EUR))))
                    .isInstanceOf(Money.InsufficientFundsException.class);
        }

        @Test
        void transference_with_inactive_source_account() {
            var source = givenAnInactiveAccount();
            var target = inactiveTargetAccount();
            accountRepo.create(source);
            accountRepo.create(target);

            assertThatThrownBy(() -> bankingApi.transferMoney(transactionWithAmount(Money.of(50.0, EUR))))
                    .isInstanceOf(AccountIsInactiveException.class);
        }

        @Test
        void transference_with_inactive_target_account() {
            var source = givenAnAccountWithBalance(100.0);
            var target = inactiveTargetAccount();
            accountRepo.create(source);
            accountRepo.create(target);

            assertThatThrownBy(() -> bankingApi.transferMoney(transactionWithAmount(Money.of(50.0, EUR))))
                    .isInstanceOf(AccountIsInactiveException.class);
        }

        @Test
        void accounts_have_distinct_currencies() {
            var source = givenAnAccountWithBalance(100.0);
            var target = givenAntargetAccountWithDifferentCurrency();
            accountRepo.create(source);
            accountRepo.create(target);

            assertThatThrownBy(() -> bankingApi.transferMoney(transactionWithAmount(Money.of(50.0, EUR))))
                    .isInstanceOf(OperationWithDifferentCurrenciesException.class);
        }
    }
}
