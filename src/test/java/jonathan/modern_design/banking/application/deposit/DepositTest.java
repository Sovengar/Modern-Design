package jonathan.modern_design.banking.application.deposit;

import jonathan.modern_design.auth.api.AuthApi;
import jonathan.modern_design.banking.api.AccountApi;
import jonathan.modern_design.banking.application.Deposit;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import jonathan.modern_design.banking.infra.AccountingConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static java.math.BigDecimal.TEN;
import static jonathan.modern_design._fake_data.AccountStub.AccountMother.sourceAccountEmpty;
import static jonathan.modern_design._shared.domain.Currency.EUR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DepositTest {
    private final AccountingConfig accountingConfig = new AccountingConfig();
    final AccountRepo accountRepo = accountingConfig.getAccountRepo();
    //why got broke @MockitoBean
    private AuthApi authApi;
    private final AccountApi accountApi = accountingConfig.accountApi(authApi);

    @Test
    void should_deposit_money_successfully() {
        var source = sourceAccountEmpty();
        var accountNumber = accountRepo.create(source);

        accountApi.deposit(new Deposit.Command(accountNumber.getAccountNumber(), TEN, EUR));

        assertThat(source.getMoney().getBalance()).isEqualTo(TEN);
    }

    @Test
    void should_throw_when_account_not_found() {
        // Arrange
        var command = new Deposit.Command("NOPE", TEN, EUR);

        // Act + Assert
        assertThrows(NoSuchElementException.class, () -> accountApi.deposit(command));
    }

    @Test
    void should_throw_when_account_not_filled() {
        // Arrange
        var command = new Deposit.Command("", null, null);

        // Act + Assert
        assertThrows(NoSuchElementException.class, () -> accountApi.deposit(command));
    }
}

