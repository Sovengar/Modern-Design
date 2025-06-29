package jonathan.modern_design.banking.application.transfer;

import jonathan.modern_design.__config.shared_for_all_classes.AceptanceTest;
import jonathan.modern_design.__config.shared_for_all_classes.EnableTestContainers;
import jonathan.modern_design._dsl.BankingDsl;
import jonathan.modern_design._shared.domain.vo.Money;
import jonathan.modern_design.banking.api.BankingApi;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static jonathan.modern_design._dsl.AccountStub.TransferMoneyMother.fromAccountToAccountWithAmount;
import static jonathan.modern_design._shared.domain.Currency.EUR;
import static org.assertj.core.api.Assertions.assertThat;

//TODO ERROR WITH FEIGN @ApplicationModuleTest //Better than @SpringBootTest when using modules
@SpringBootTest
@AceptanceTest
@EnableTestContainers
class TransferMoneyIT {
    @Autowired
    private BankingApi bankingApi;

    @Autowired
    private AccountRepo repository;

    @Nested
    class WithValidAccountsShould {
        @Test
        void transfer_money_into_the_target_account() {
            var source = BankingDsl.givenAnAccountWithMoney(Money.of(BigDecimal.valueOf(100.0), EUR), bankingApi, repository);
            var target = BankingDsl.givenAnAccountWithMoney(Money.of(ZERO, EUR), bankingApi, repository);

            var command = fromAccountToAccountWithAmount(source.getAccountNumber().getAccountNumber(), target.getAccountNumber().getAccountNumber(), Money.of(BigDecimal.valueOf(60.0), EUR));
            bankingApi.transferMoney(command);

            var sourceResult = repository.findByAccNumberOrElseThrow(source.getAccountNumber().getAccountNumber());
            var targetResult = repository.findByAccNumberOrElseThrow(target.getAccountNumber().getAccountNumber());

            assertThat(sourceResult.getMoney().getBalance()).isEqualTo(BigDecimal.valueOf(40.0));
            assertThat(targetResult.getMoney().getBalance()).isEqualTo(BigDecimal.valueOf(60.0));
        }

        @Test
        void transfer_money_into_the_target_account_check_source_approval() {
            var source = BankingDsl.givenAnAccountWithMoney(Money.of(BigDecimal.valueOf(100.0), EUR), bankingApi, repository);
            var target = BankingDsl.givenAnAccountWithMoney(Money.of(ZERO, EUR), bankingApi, repository);

            var command = fromAccountToAccountWithAmount(source.getAccountNumber().getAccountNumber(), target.getAccountNumber().getAccountNumber(), Money.of(BigDecimal.valueOf(50.0), EUR));
            bankingApi.transferMoney(command);

            var sourceResult = repository.findByAccNumberOrElseThrow(source.getAccountNumber().getAccountNumber());
            Approvals.verify(sourceResult.getMoney().getBalance());
        }

        @Test
        void transfer_money_into_the_target_account_check_target_approval() {
            var source = BankingDsl.givenAnAccountWithMoney(Money.of(BigDecimal.valueOf(100.0), EUR), bankingApi, repository);
            var target = BankingDsl.givenAnAccountWithMoney(Money.of(ZERO, EUR), bankingApi, repository);

            var command = fromAccountToAccountWithAmount(source.getAccountNumber().getAccountNumber(), target.getAccountNumber().getAccountNumber(), Money.of(BigDecimal.valueOf(50.0), EUR));
            bankingApi.transferMoney(command);

            var targetResult = repository.findByAccNumberOrElseThrow(target.getAccountNumber().getAccountNumber());
            Approvals.verify(targetResult.getMoney().getBalance());
        }
    }
}
