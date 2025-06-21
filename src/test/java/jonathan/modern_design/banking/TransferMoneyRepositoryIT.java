package jonathan.modern_design.banking;

import jonathan.modern_design.__config.ITConfig;
import jonathan.modern_design.banking.api.AccountApi;
import jonathan.modern_design.banking.application.Deposit;
import jonathan.modern_design.banking.domain.models.account.Account;
import jonathan.modern_design.banking.domain.models.account.vo.AccountMoney;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import jonathan.modern_design.user.api.UserApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static jonathan.modern_design._fake_data.AccountStub.CreateAccountMother.randomAccountWithCurrency;
import static jonathan.modern_design._fake_data.AccountStub.TransferMoneyMother.fromAccountToAccountWithAmount;
import static jonathan.modern_design._shared.Currency.EUR;
import static org.assertj.core.api.Assertions.assertThat;

class TransferMoneyRepositoryIT extends ITConfig {
    @Autowired
    private AccountApi accountFacade;
    @Autowired
    private AccountRepo repository;
    //@MockitoBean
    private UserApi userApi;

    private Account getAccountWithMoney(final AccountMoney money) {
        Assertions.assertNotNull(money.getCurrency());
        var accountNumber = accountFacade.createAccount(randomAccountWithCurrency(money.getCurrency())).getAccountNumber();

        if (money.checkPositive()) {
            accountFacade.deposit(new Deposit.Command(accountNumber, money.getBalance(), money.getCurrency()));
        }

        return repository.findByAccNumberOrElseThrow(accountNumber);
    }

    @Nested
    class WithValidAccountsShould {
        @Test
        void transfer_money_into_the_target_account() {
            var source = getAccountWithMoney(AccountMoney.of(BigDecimal.valueOf(100.0), EUR));
            var target = getAccountWithMoney(AccountMoney.of(ZERO, EUR));
            accountFacade.transferMoney(fromAccountToAccountWithAmount(source.getAccountNumber().getAccountNumber(), target.getAccountNumber().getAccountNumber(), AccountMoney.of(BigDecimal.valueOf(60.0), EUR)));

            assertThat(source.getMoney().getBalance()).isEqualTo(BigDecimal.valueOf(40.0));
            assertThat(target.getMoney().getBalance()).isEqualTo(BigDecimal.valueOf(60.0));
        }
    }
}
