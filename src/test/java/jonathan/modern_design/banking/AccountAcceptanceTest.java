package jonathan.modern_design.banking;

import com.fasterxml.jackson.databind.ObjectMapper;
import jonathan.modern_design.__config.ITConfig;
import jonathan.modern_design._fake_data.AccountStub;
import jonathan.modern_design._shared.vo.AccountMoney;
import jonathan.modern_design.banking.api.AccountApi;
import jonathan.modern_design.banking.application.Deposit;
import jonathan.modern_design.banking.domain.models.Account;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static jonathan.modern_design._fake_data.AccountStub.CreateAccountMother.randomAccountWithCurrency;
import static jonathan.modern_design._fake_data.AccountStub.TransferMoneyMother.fromAccountToAccountWithAmount;
import static jonathan.modern_design._shared.Currency.EUR;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

final class AccountAcceptanceTest extends ITConfig {
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private AccountRepo repository;

    @Autowired
    private AccountApi accountFacade;

    private Account getAccountWithMoney(final AccountMoney money) {
        Assertions.assertNotNull(money.getCurrency());
        var accountNumber = accountFacade.createAccount(randomAccountWithCurrency(money.getCurrency())).getAccountNumber();

        if (money.checkPositive()) {
            accountFacade.deposit(new Deposit.Command(accountNumber, money.getBalance(), money.getCurrency()));
        }

        return repository.findByAccNumberOrElseThrow(accountNumber);
    }

    @Nested
    class WithValidDataForCreatingAccountShould {
        @Test
        void create_account() throws Exception {
            String json = mapper.writeValueAsString(AccountStub.CreateAccountMother.createAccountCommandWithValidData());
            //Use jsonPath("$.starships") ?

            mockMvc.perform(post("/api/v1/accounts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    class WithValidAccountsShould {
        @Test
        void transfer_money_into_the_target_account_check_source_approval() {
            var source = getAccountWithMoney(AccountMoney.of(BigDecimal.valueOf(100.0), EUR));
            var target = getAccountWithMoney(AccountMoney.of(ZERO, EUR));

            accountFacade.transferMoney(fromAccountToAccountWithAmount(
                    source.getAccountNumber().getAccountNumber(),
                    target.getAccountNumber().getAccountNumber(),
                    AccountMoney.of(BigDecimal.valueOf(50.0), EUR))
            );

            source = repository.findByAccNumberOrElseThrow(source.getAccountNumber().getAccountNumber());
            Approvals.verify(source.getMoney().getBalance());
        }

        @Test
        void transfer_money_into_the_target_account_check_target_approval() {
            var source = getAccountWithMoney(AccountMoney.of(BigDecimal.valueOf(100.0), EUR));
            var target = getAccountWithMoney(AccountMoney.of(ZERO, EUR));

            accountFacade.transferMoney(fromAccountToAccountWithAmount(
                    source.getAccountNumber().getAccountNumber(),
                    target.getAccountNumber().getAccountNumber(),
                    AccountMoney.of(BigDecimal.valueOf(50.0), EUR))
            );

            target = repository.findByAccNumberOrElseThrow(target.getAccountNumber().getAccountNumber());
            Approvals.verify(target.getMoney().getBalance());
        }
    }
}


