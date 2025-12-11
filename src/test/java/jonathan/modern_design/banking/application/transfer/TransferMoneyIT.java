package jonathan.modern_design.banking.application.transfer;

import jonathan.modern_design.__config.shared_for_all_classes.AcceptanceTest;
import jonathan.modern_design.__config.shared_for_all_classes.EnableTestContainers;
import jonathan.modern_design._shared.domain.vo.Money;
import jonathan.modern_design.banking.BankingDsl;
import jonathan.modern_design.banking.application.TransferMoney;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static jonathan.modern_design._shared.domain.catalogs.Currency.EUR;
import static jonathan.modern_design.banking.application.transfer.TransferMoneyDsl.fromAccountToAccountWithAmount;
import static jonathan.modern_design.banking.domain.AccountStub.DEFAULT_SOURCE_ACCOUNT_NUMBER;
import static jonathan.modern_design.banking.domain.AccountStub.DEFAULT_TARGET_ACCOUNT_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;

//TODO ERROR WITH FEIGN @ApplicationModuleTest //Better than @SpringBootTest when using modules
@SpringBootTest
@AcceptanceTest
@EnableTestContainers
class TransferMoneyIT extends BankingDsl {
    @Autowired
    private TransferMoney transferMoney;

    @Autowired
    private AccountRepo repository;

    @Nested
    class WithValidAccountsShould {
        @Test
        void transfer_money_into_the_target_account() {
            var source = givenAnAccountWithBalance(100.0, DEFAULT_SOURCE_ACCOUNT_NUMBER);
            var target = givenAnAccountWithBalance(0.0, DEFAULT_TARGET_ACCOUNT_NUMBER);

            var command = fromAccountToAccountWithAmount(source.getAccountNumber(), target.getAccountNumber(), Money.of(60.0, EUR));
            transferMoney.handle(command);

            var sourceResult = repository.findByAccNumberOrElseThrow(source.getAccountNumber());
            var targetResult = repository.findByAccNumberOrElseThrow(target.getAccountNumber());

            assertThat(sourceResult.getMoney().getBalance()).isEqualTo(BigDecimal.valueOf(40.0));
            assertThat(targetResult.getMoney().getBalance()).isEqualTo(BigDecimal.valueOf(60.0));
        }

        @Test
        void transfer_money_into_the_target_account_check_source_approval() {
            var source = givenAnAccountWithBalance(100.0, DEFAULT_SOURCE_ACCOUNT_NUMBER);
            var target = givenAnAccountWithBalance(0.0, DEFAULT_TARGET_ACCOUNT_NUMBER);

            var command = fromAccountToAccountWithAmount(source.getAccountNumber(), target.getAccountNumber(), Money.of(50.0, EUR));
            transferMoney.handle(command);

            var sourceResult = repository.findByAccNumberOrElseThrow(source.getAccountNumber());
            Approvals.verify(sourceResult.getMoney().getBalance());
        }

        @Test
        void transfer_money_into_the_target_account_check_target_approval() {
            var source = givenAnAccountWithBalance(100.0, DEFAULT_SOURCE_ACCOUNT_NUMBER);
            var target = givenAnAccountWithBalance(0.0, DEFAULT_TARGET_ACCOUNT_NUMBER);

            var command = fromAccountToAccountWithAmount(source.getAccountNumber(), target.getAccountNumber(), Money.of(50.0, EUR));
            transferMoney.handle(command);

            var targetResult = repository.findByAccNumberOrElseThrow(target.getAccountNumber());
            Approvals.verify(targetResult.getMoney().getBalance());
        }
    }
}
