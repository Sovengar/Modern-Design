package jonathan.modern_design.banking.queries;

import jonathan.modern_design.__config.shared_for_all_classes.AcceptanceTest;
import jonathan.modern_design.__config.shared_for_all_classes.EnableTestContainers;
import jonathan.modern_design.banking.BankingAcceptanceConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AcceptanceTest
@EnableTestContainers
class SearchAccountAcceptanceIT extends BankingAcceptanceConfig {

    @Test
    void should_search_accounts() throws Exception {
        givenARandomAccountWithBalance(10.0);
        givenARandomAccountWithBalance(10.0);
        givenARandomAccountWithBalance(10.0);

        givenARandomAccountWithBalance(10.0);
        givenARandomAccountWithBalance(10.0);
        givenARandomAccountWithBalance(10.0);

        givenARandomAccountWithBalance(10.0);

        String jsonFilters = """
                {}
                """;

        mockMvc.perform(post("/banking/v1/accounts/search/xxxPage")
                        .param("page", "0")
                        .param("size", "3")
                        .content(jsonFilters)
                        .contentType(MediaType.APPLICATION_JSON))
                //.andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.totalElements", is(7)))
                .andExpect(jsonPath("$.totalPages", is(3)))
                .andExpect(jsonPath("$.content[0].data", is("DummyData")))
        ;
    }
}
