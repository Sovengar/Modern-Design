package com.jonathan.modern_design.account_module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonathan.modern_design.__config.ITConfig;
import com.jonathan.modern_design._fake_data.CreateAccountMother;
import com.jonathan.modern_design.account_module.application.AccountFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(AccountConfiguration.class)
final class AccountTestCase extends ITConfig {
    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private AccountFacade accountFacade;

    @Test
    void should_create_account() throws Exception {
        String json = mapper.writeValueAsString(CreateAccountMother.createAccountCommandWithValidData());

        mockMvc.perform(post("/api/v1/accounts/a")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
        //.andExpect(jsonPath("$.id").isNotEmpty())
        //.andExpect(jsonPath("$.starships").value(hasSize(1)))
        //.andExpect(jsonPath("$.starships[0].name").value("Millennium Falcon"))
        //.andExpect(jsonPath("$.starships[0].capacity").value("6"));
    }

//    @TestConfiguration
//    @ComponentScan(
//            basePackageClasses = {AccountConfiguration.class},
//            includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Stub.class})})
//    static class StubConfiguration {
//    }

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


}


