package com.jonathan.modern_design.account_module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonathan.modern_design.__config.ITConfig;
import com.jonathan.modern_design._fake_data.CreateAccountMother;
import com.jonathan.modern_design._fake_data.CreateAccountStub;
import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.account_module.application.AccountFacade;
import com.jonathan.modern_design.account_module.application.deposit.DepositCommand;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.infra.persistence.AccountPersistenceAdapter;
import lombok.val;
import org.approvaltests.Approvals;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static com.jonathan.modern_design._fake_data.SendMoneyMother.fromAccountToAccountWithAmount;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

final class AccountAceptanceTest extends ITConfig {
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private AccountPersistenceAdapter repository;

    @Autowired
    private AccountFacade accountFacade;

    @Test
    void should_create_account() throws Exception {
        String json = mapper.writeValueAsString(CreateAccountMother.createAccountCommandWithValidData());

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
        //.andExpect(jsonPath("$.id").isNotEmpty())
        //.andExpect(jsonPath("$.starships").value(hasSize(1)))
        //.andExpect(jsonPath("$.starships[0].name").value("Millennium Falcon"))
        //.andExpect(jsonPath("$.starships[0].capacity").value("6"));
    }

    @Test
    void approval_test_transfer_money_into_the_target_account_check_source() {
        val accounts = getAccountsAfterTransfer(50.0);
        Approvals.verify(accounts.source().getMoney().getBalance());
    }

    @Test
    void approval_test_transfer_money_into_the_target_account_check_target() {
        val accounts = getAccountsAfterTransfer(50.0);
        Approvals.verify(accounts.target().getMoney().getBalance());
    }

    //    @Test
//    void should_return_a_fleet_given_an_id() throws Exception {
//        Fleet fleet = new Fleet(singletonList(
//                new StarShip("Millennium Falcon", 6, new BigDecimal("100000"))
//        ));
//        fleets.save(fleet);
//
//        mockMvc.perform(
//                        get("/rescueFleets/%s".formatted(fleet.id()))
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//        //.andExpect(jsonPath("$.id").value(fleet.id().toString()))
//        //.andExpect(jsonPath("$.starships").value(hasSize(fleet.starships().size())))
//        //.andExpect(jsonPath("$.starships[0].name").value(fleet.starships().get(0).name()))
//        //.andExpect(jsonPath("$.starships[0].capacity").value(fleet.starships().get(0).passengersCapacity()));
//    }
    private @NotNull AccountsAfterTransfer getAccountsAfterTransfer(final double amount) {
        var source = accountFacade.createAccount(CreateAccountStub.randomAccountWithCurrency(Currency.EURO));
        source = accountFacade.deposit(new DepositCommand(source.getAccountNumber(), BigDecimal.valueOf(100), Currency.EURO));
        var target = accountFacade.createAccount(CreateAccountStub.randomAccountWithCurrency(Currency.EURO));

        accountFacade.transferMoney(fromAccountToAccountWithAmount(source.getAccountNumber(), target.getAccountNumber(), 60.0));

        source = repository.findOne(source.getAccountNumber()).orElseThrow();
        target = repository.findOne(target.getAccountNumber()).orElseThrow();

        AccountsAfterTransfer result = new AccountsAfterTransfer(source, target);
        return result;
    }

    private record AccountsAfterTransfer(Account source, Account target) {
    }

}


