package jonathan.modern_design.banking.application.deposit;

import jonathan.modern_design.__config.shared_for_all_classes.UnitTest;
import jonathan.modern_design.auth.api.AuthApi;
import jonathan.modern_design.banking.api.BankingApi;
import jonathan.modern_design.banking.application.Deposit;
import jonathan.modern_design.banking.domain.exceptions.AccountNotFoundException;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import jonathan.modern_design.banking.infra.AccountingConfig;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static java.math.BigDecimal.TEN;
import static jonathan.modern_design._dsl.AccountStub.AccountMother.emptyAccount;
import static jonathan.modern_design._shared.domain.catalogs.Currency.EUR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@UnitTest
class DepositTest {
    private final AccountingConfig accountingConfig = new AccountingConfig();
    final AccountRepo accountRepo = accountingConfig.getAccountRepo();
    @MockitoBean
    private AuthApi authApi;
    private final BankingApi bankingApi = accountingConfig.accountApi(authApi);

    @Test
    void should_deposit_money_successfully() {
        var source = emptyAccount();
        var accountNumber = accountRepo.create(source);

        bankingApi.deposit(new Deposit.Command(accountNumber.getAccountNumber(), TEN, EUR));

        assertThat(source.getMoney().getBalance()).isEqualTo(TEN);
    }

    @Test
    void should_throw_when_account_not_found() {
        // Arrange
        var command = new Deposit.Command("NOPE", TEN, EUR);

        // Act + Assert
        assertThrows(AccountNotFoundException.class, () -> bankingApi.deposit(command));
    }

    @Test
    void should_throw_when_account_not_filled() {
        // Arrange
        var command = new Deposit.Command("", null, null);

        // Act + Assert
        assertThrows(AccountNotFoundException.class, () -> bankingApi.deposit(command));
    }
}

