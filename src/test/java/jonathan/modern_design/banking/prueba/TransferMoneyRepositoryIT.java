package jonathan.modern_design.banking.prueba;

import jonathan.modern_design.__config.IntegrationConfig;
import jonathan.modern_design.__config.shared_for_all_classes.EnableTestContainers;
import jonathan.modern_design._shared.domain.vo.Money;
import jonathan.modern_design.banking.api.AccountApi;
import jonathan.modern_design.banking.application.Deposit;
import jonathan.modern_design.banking.domain.models.Account;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static jonathan.modern_design.__config.dsl.AccountStub.CreateAccountMother.createAccountCommand;
import static jonathan.modern_design.__config.dsl.AccountStub.TransferMoneyMother.fromAccountToAccountWithAmount;
import static jonathan.modern_design._shared.domain.Currency.EUR;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@IntegrationConfig
@EnableTestContainers
class TransferMoneyRepositoryIT {
    @Autowired
    private AccountApi accountFacade;
    @Autowired
    private AccountRepo repository;

    private Account getAccountWithMoney(final Money money) {
        Assertions.assertNotNull(money.getCurrency());
        var accountNumber = accountFacade.createAccount(createAccountCommand(money.getCurrency().getCode())).getAccountNumber();

        if (money.checkPositive()) {
            accountFacade.deposit(new Deposit.Command(accountNumber, money.getBalance(), money.getCurrency()));
        }

        return repository.findByAccNumberOrElseThrow(accountNumber);
    }

    @Nested
    class WithValidAccountsShould {
        @Test
        void transfer_money_into_the_target_account() {
            var source = getAccountWithMoney(Money.of(BigDecimal.valueOf(100.0), EUR));
            var target = getAccountWithMoney(Money.of(ZERO, EUR));

            var command = fromAccountToAccountWithAmount(source.getAccountNumber().getAccountNumber(), target.getAccountNumber().getAccountNumber(), Money.of(BigDecimal.valueOf(60.0), EUR));
            accountFacade.transferMoney(command);

            var sourceResult = repository.findByAccNumberOrElseThrow(source.getAccountNumber().getAccountNumber());
            var targetResult = repository.findByAccNumberOrElseThrow(target.getAccountNumber().getAccountNumber());

            assertThat(sourceResult.getMoney().getBalance()).isEqualTo(BigDecimal.valueOf(40.0));
            assertThat(targetResult.getMoney().getBalance()).isEqualTo(BigDecimal.valueOf(60.0));
        }
    }
}
