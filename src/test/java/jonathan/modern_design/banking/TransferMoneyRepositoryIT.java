package jonathan.modern_design.banking;

import jonathan.modern_design.__config.ITConfig;
import jonathan.modern_design._shared.domain.vo.Money;
import jonathan.modern_design.auth.api.AuthApi;
import jonathan.modern_design.banking.api.AccountApi;
import jonathan.modern_design.banking.application.Deposit;
import jonathan.modern_design.banking.domain.models.Account;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static jonathan.modern_design._fake_data.AccountStub.CreateAccountMother.createAccountCommand;
import static jonathan.modern_design._fake_data.AccountStub.TransferMoneyMother.fromAccountToAccountWithAmount;
import static jonathan.modern_design._shared.domain.Currency.EUR;
import static org.assertj.core.api.Assertions.assertThat;

class TransferMoneyRepositoryIT extends ITConfig {
    @Autowired
    private AccountApi accountFacade;
    @Autowired
    private AccountRepo repository;
    //@MockitoBean
    private AuthApi authApi;

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
            accountFacade.transferMoney(fromAccountToAccountWithAmount(source.getAccountNumber().getAccountNumber(), target.getAccountNumber().getAccountNumber(), Money.of(BigDecimal.valueOf(60.0), EUR)));

            assertThat(source.getMoney().getBalance()).isEqualTo(BigDecimal.valueOf(40.0));
            assertThat(target.getMoney().getBalance()).isEqualTo(BigDecimal.valueOf(60.0));
        }
    }
}
