package jonathan.modern_design.banking.application.deposit;

import jonathan.modern_design._shared.domain.vo.Money;
import jonathan.modern_design.banking.BankingUnitConfig;
import jonathan.modern_design.banking.application.Deposit;
import jonathan.modern_design.banking.domain.exceptions.AccountNotFoundException;
import org.junit.jupiter.api.Test;

import static java.math.BigDecimal.TEN;
import static jonathan.modern_design._shared.domain.catalogs.Currency.EUR;
import static jonathan.modern_design.banking.AccountStub.AccountMother.givenAnEmptyAccount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DepositTest extends BankingUnitConfig {
    @Test
    void should_deposit_money_successfully() {
        var account = givenAnEmptyAccount();
        account.deposit(Money.of(TEN, EUR));
        assertThat(account.getMoney().getBalance()).isEqualTo(TEN);
    }

    @Test
    void should_deposit_money_successfully_orchestration() {
        var account = givenAnEmptyAccount();
        var accountNumber = accountRepo.create(account);
        bankingApi.deposit(new Deposit.Command(accountNumber.getAccountNumber(), TEN, EUR));
        assertThat(account.getMoney().getBalance()).isEqualTo(TEN);
    }

    @Test
    void should_throw_when_account_not_found() {
        var command = new Deposit.Command("NOPE", TEN, EUR);
        assertThrows(AccountNotFoundException.class, () -> bankingApi.deposit(command));
    }

    @Test
    void should_throw_when_account_not_filled() {
        var command = new Deposit.Command("", null, null);
        assertThrows(AccountNotFoundException.class, () -> bankingApi.deposit(command));
    }
}

