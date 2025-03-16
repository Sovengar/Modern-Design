package jonathan.modern_design.account_module;

import jonathan.modern_design.__config.ITConfig;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.AccountRepo;
import jonathan.modern_design.account_module.domain.vo.AccountMoney;
import jonathan.modern_design.account_module.dtos.DepositCommand;
import jonathan.modern_design.user.UserApi;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static jonathan.modern_design._fake_data.AccountStub.CreateAccountMother.randomAccountWithCurrency;
import static jonathan.modern_design._fake_data.AccountStub.TransferMoneyMother.fromAccountToAccountWithAmount;
import static jonathan.modern_design._shared.Currency.EURO;
import static org.assertj.core.api.Assertions.assertThat;

class TransferMoneyRepositoryIT extends ITConfig {
    @Autowired
    private AccountApi accountFacade;
    @Autowired
    private AccountRepo repository;
    @MockitoBean
    private UserApi userApi;

    private Account getAccountWithMoney(final AccountMoney money) {
        var accountNumber = accountFacade.createAccount(randomAccountWithCurrency(money.getCurrency())).getValue();

        if (money.isPositive()) {
            accountFacade.deposit(new DepositCommand(accountNumber, money.getAmount(), money.getCurrency()));
        }

        return repository.findOne(accountNumber).orElseThrow();
    }

    @Nested
    class WithValidAccountsShould {
        @Test
        void transfer_money_into_the_target_account() {
            var source = getAccountWithMoney(AccountMoney.of(BigDecimal.valueOf(100.0), EURO));
            var target = getAccountWithMoney(AccountMoney.of(ZERO, EURO));
            accountFacade.transferMoney(fromAccountToAccountWithAmount(source.getAccountNumber().getValue(), target.getAccountNumber().getValue(), AccountMoney.of(BigDecimal.valueOf(60.0), EURO)));

            assertThat(source.getMoney().getAmount()).isEqualTo(BigDecimal.valueOf(40.0));
            assertThat(target.getMoney().getAmount()).isEqualTo(BigDecimal.valueOf(60.0));
        }
    }
}
