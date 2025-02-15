package com.jonathan.modern_design.account_module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonathan.modern_design.__config.ITConfig;
import com.jonathan.modern_design._fake_data.AccountStub;
import com.jonathan.modern_design._fake_data.CreateAccountMother;
import com.jonathan.modern_design.account_module.application.AccountFacade;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.infra.persistence.AccountRepositorySpringAdapter;
import lombok.val;
import org.approvaltests.Approvals;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static com.jonathan.modern_design._fake_data.SendMoneyMother.transactionWithAmount;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

final class AccountAceptanceTest extends ITConfig {
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private AccountRepositorySpringAdapter repository;

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
    private @NotNull AccountsAfterTransfer getAccountsAfterTransfer(final double amount) {
        Account source = repository.create(AccountStub.sourceAccountwithBalance(100.0));
        Account target = repository.create(AccountStub.targetAccountEmpty());

        accountFacade.transferMoney(transactionWithAmount(amount));

        source = repository.findOne(source.getAccountNumber()).orElseThrow();
        target = repository.findOne(target.getAccountNumber()).orElseThrow();
        AccountsAfterTransfer result = new AccountsAfterTransfer(source, target);
        return result;
    }

    private record AccountsAfterTransfer(Account source, Account target) {
    }

}


