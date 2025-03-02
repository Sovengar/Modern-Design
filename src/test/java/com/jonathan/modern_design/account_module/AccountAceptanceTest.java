package com.jonathan.modern_design.account_module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonathan.modern_design.__config.ITConfig;
import com.jonathan.modern_design._fake_data.AccountStub;
import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.account_module.application.DepositUseCase;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.infra.persistence.AccountPersistenceAdapter;
import lombok.val;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static com.jonathan.modern_design._fake_data.AccountStub.CreateAccountMother.randomAccountWithCurrency;
import static com.jonathan.modern_design._fake_data.AccountStub.TransferMoneyMother.fromAccountToAccountWithAmount;
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
        String json = mapper.writeValueAsString(AccountStub.CreateAccountMother.createAccountCommandWithValidData());

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
        Approvals.verify(accounts.source().getMoney().getAmount());
    }

    @Test
    void approval_test_transfer_money_into_the_target_account_check_target() {
        val accounts = getAccountsAfterTransfer(50.0);
        Approvals.verify(accounts.target().getMoney().getAmount());
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
    private AccountsAfterTransfer getAccountsAfterTransfer(final double amount) {
        //Source
        var sourceNumber = accountFacade.createAccount(randomAccountWithCurrency(Currency.EURO)).getValue();
        accountFacade.deposit(new DepositUseCase.DepositCommand(sourceNumber, BigDecimal.valueOf(100), Currency.EURO));
        var targetNumber = accountFacade.createAccount(randomAccountWithCurrency(Currency.EURO)).getValue();

        //Last
        accountFacade.transferMoney(fromAccountToAccountWithAmount(sourceNumber, targetNumber, 60.0));
        var source = repository.findOne(sourceNumber).orElseThrow();
        var target = repository.findOne(targetNumber).orElseThrow();

        return new AccountsAfterTransfer(source, target);
    }

    private record AccountsAfterTransfer(Account source, Account target) {
    }

}


