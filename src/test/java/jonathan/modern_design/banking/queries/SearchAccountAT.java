package jonathan.modern_design.banking.queries;

import jonathan.modern_design.__config.initializers.InfraInitializer;
import jonathan.modern_design.__config.runners.AcceptanceRunner;
import jonathan.modern_design.banking.BankingAcceptanceConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AcceptanceRunner
@InfraInitializer
class SearchAccountAT extends BankingAcceptanceConfig {

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
                .andExpect(jsonPath("$.data.content", hasSize(3)))
                .andExpect(jsonPath("$.data.totalElements", is(7)))
                .andExpect(jsonPath("$.data.totalPages", is(3)));
    }
}
