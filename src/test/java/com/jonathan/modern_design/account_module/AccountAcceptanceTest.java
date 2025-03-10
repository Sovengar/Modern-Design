package com.jonathan.modern_design.account_module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonathan.modern_design.__config.ITConfig;
import com.jonathan.modern_design._fake_data.AccountStub;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountMoney;
import com.jonathan.modern_design.account_module.dtos.DepositCommand;
import com.jonathan.modern_design.account_module.infra.persistence.AccountRepoAdapter;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static com.jonathan.modern_design._fake_data.AccountStub.CreateAccountMother.randomAccountWithCurrency;
import static com.jonathan.modern_design._fake_data.AccountStub.TransferMoneyMother.fromAccountToAccountWithAmount;
import static com.jonathan.modern_design._shared.Currency.EURO;
import static java.math.BigDecimal.ZERO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

final class AccountAcceptanceTest extends ITConfig {
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private AccountRepoAdapter repository;

    @Autowired
    private AccountApi accountFacade;

    @Test
    void should_create_account() throws Exception {
        String json = mapper.writeValueAsString(AccountStub.CreateAccountMother.createAccountCommandWithValidData());
        //Use jsonPath("$.starships") ?

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void approval_test_transfer_money_into_the_target_account_check_source() {
        var source = getAccountWithMoney(AccountMoney.of(BigDecimal.valueOf(100.0), EURO));
        var target = getAccountWithMoney(AccountMoney.of(ZERO, EURO));
        accountFacade.transferMoney(fromAccountToAccountWithAmount(source.getAccountNumber().getValue(), target.getAccountNumber().getValue(), AccountMoney.of(BigDecimal.valueOf(50.0), EURO)));

        source = repository.findOne(source.getAccountNumber().getValue()).orElseThrow();
        Approvals.verify(source.getMoney().getAmount());
    }

    @Test
    void approval_test_transfer_money_into_the_target_account_check_target() {
        var source = getAccountWithMoney(AccountMoney.of(BigDecimal.valueOf(100.0), EURO));
        var target = getAccountWithMoney(AccountMoney.of(ZERO, EURO));
        accountFacade.transferMoney(fromAccountToAccountWithAmount(source.getAccountNumber().getValue(), target.getAccountNumber().getValue(), AccountMoney.of(BigDecimal.valueOf(50.0), EURO)));

        target = repository.findOne(target.getAccountNumber().getValue()).orElseThrow();
        Approvals.verify(target.getMoney().getAmount());
    }

    private Account getAccountWithMoney(final AccountMoney money) {
        var accountNumber = accountFacade.createAccount(randomAccountWithCurrency(money.getCurrency())).getValue();

        if (money.isPositive()) {
            accountFacade.deposit(new DepositCommand(accountNumber, money.getAmount(), money.getCurrency()));
        }

        return repository.findOne(accountNumber).orElseThrow();
    }
}


