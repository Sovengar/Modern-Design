package jonathan.modern_design.account_module;

import jonathan.modern_design.__config.ITConfig;
import jonathan.modern_design.account_module.api.AccountApi;
import jonathan.modern_design.account_module.application.Deposit;
import jonathan.modern_design.account_module.domain.models.account.Account;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountMoney;
import jonathan.modern_design.account_module.domain.store.AccountRepo;
import jonathan.modern_design.user.api.UserApi;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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
    @MockitoBean
    private UserApi userApi;

    private Account getAccountWithMoney(final AccountMoney money) {
        var accountNumber = accountFacade.createAccount(randomAccountWithCurrency(money.currency())).accountNumber();

        if (money.isPositive()) {
            accountFacade.deposit(new Deposit.Command(accountNumber, money.balance(), money.currency()));
        }

        return repository.findByAccNumberOrElseThrow(accountNumber);
    }

    @Nested
    class WithValidAccountsShould {
        @Test
        void transfer_money_into_the_target_account() {
            var source = getAccountWithMoney(AccountMoney.of(BigDecimal.valueOf(100.0), EUR));
            var target = getAccountWithMoney(AccountMoney.of(ZERO, EUR));
            accountFacade.transferMoney(fromAccountToAccountWithAmount(source.accountAccountNumber().accountNumber(), target.accountAccountNumber().accountNumber(), AccountMoney.of(BigDecimal.valueOf(60.0), EUR)));

            assertThat(source.money().balance()).isEqualTo(BigDecimal.valueOf(40.0));
            assertThat(target.money().balance()).isEqualTo(BigDecimal.valueOf(60.0));
        }
    }
}
