package jonathan.modern_design.account_module.application.deposit;

import jonathan.modern_design.account_module.api.AccountApi;
import jonathan.modern_design.account_module.application.Deposit;
import jonathan.modern_design.account_module.domain.store.AccountRepo;
import jonathan.modern_design.account_module.infra.AccountingConfig;
import jonathan.modern_design.user.api.UserApi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.NoSuchElementException;

import static java.math.BigDecimal.TEN;
import static jonathan.modern_design._fake_data.AccountStub.AccountMother.sourceAccountEmpty;
import static jonathan.modern_design._shared.Currency.EUR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DepositTest {
    private final AccountingConfig accountingConfig = new AccountingConfig();
    final AccountRepo accountRepo = accountingConfig.getAccountRepo();
    @MockitoBean
    private UserApi userApi;
    private final AccountApi accountApi = accountingConfig.accountApi(userApi);

    @Test
    void should_deposit_money_successfully() {
        var source = sourceAccountEmpty();
        var accountNumber = accountRepo.create(source);

        accountApi.deposit(new Deposit.Command(accountNumber.accountNumber(), TEN, EUR));

        assertThat(source.money().balance()).isEqualTo(TEN);
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

