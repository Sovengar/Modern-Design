package jonathan.modern_design.account_module;

import com.fasterxml.jackson.databind.ObjectMapper;
import jonathan.modern_design.__config.ITConfig;
import jonathan.modern_design._fake_data.AccountStub;
import jonathan.modern_design.account_module.api.AccountApi;
import jonathan.modern_design.account_module.application.Deposit;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.store.AccountRepo;
import jonathan.modern_design.account_module.domain.vo.AccountMoney;
import org.approvaltests.Approvals;
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
        var accountNumber = accountFacade.createAccount(randomAccountWithCurrency(money.currency())).accountNumber();

        if (money.isPositive()) {
            accountFacade.deposit(new Deposit.Command(accountNumber, money.amount(), money.currency()));
        }

        return repository.findOneOrElseThrow(accountNumber);
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
            accountFacade.transferMoney(fromAccountToAccountWithAmount(source.accountAccountNumber().accountNumber(), target.accountAccountNumber().accountNumber(), AccountMoney.of(BigDecimal.valueOf(50.0), EUR)));

            source = repository.findOneOrElseThrow(source.accountAccountNumber().accountNumber());
            Approvals.verify(source.money().amount());
        }

        @Test
        void transfer_money_into_the_target_account_check_target_approval() {
            var source = getAccountWithMoney(AccountMoney.of(BigDecimal.valueOf(100.0), EUR));
            var target = getAccountWithMoney(AccountMoney.of(ZERO, EUR));
            accountFacade.transferMoney(fromAccountToAccountWithAmount(source.accountAccountNumber().accountNumber(), target.accountAccountNumber().accountNumber(), AccountMoney.of(BigDecimal.valueOf(50.0), EUR)));

            target = repository.findOneOrElseThrow(target.accountAccountNumber().accountNumber());
            Approvals.verify(target.money().amount());
        }
    }
}


