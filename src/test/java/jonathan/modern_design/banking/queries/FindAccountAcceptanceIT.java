package jonathan.modern_design.banking.queries;

import jonathan.modern_design.__config.shared_for_all_classes.AcceptanceTest;
import jonathan.modern_design.__config.shared_for_all_classes.EnableTestContainers;
import jonathan.modern_design.banking.BankingAcceptanceConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static jonathan.modern_design.banking.AccountStub.DEFAULT_ACCOUNT_NUMBER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AcceptanceTest
@EnableTestContainers
class FindAccountAcceptanceIT extends BankingAcceptanceConfig {

    @Test
    void should_find_account_by_number() throws Exception {
        givenAnEmptyAccount();
        givenARandomAccountWithBalance(10.0);

        mockMvc.perform(get("/banking/v1/accounts/" + DEFAULT_ACCOUNT_NUMBER)
                        //.param("page", "0")
                        //.param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //.andExpect(jsonPath("$.content", hasSize(0)))
        //        .andExpect(jsonPath("$.totalElements", is(0)))
        //        .andExpect(jsonPath("$.totalPages", is(0)));
        //
        //        .andExpect(jsonPath("$.content", hasSize(2)))
        //        .andExpect(jsonPath("$.content[0].data", is("DummyData")))
        //        .andExpect(jsonPath("$.content[1].data", is("DummyData")));
        //
        //        .andExpect(jsonPath("$.data", is("DummyData")));
    }

    @Test
    void should_not_find_account_by_number() throws Exception {
        givenARandomAccountWithBalance(10.0);

        mockMvc.perform(get("/banking/v1/accounts/" + DEFAULT_ACCOUNT_NUMBER)
                        //.param("page", "0")
                        //.param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
