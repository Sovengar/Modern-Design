package jonathan.modern_design.banking.queries;

import jonathan.modern_design.__config.runners.AcceptanceITRunner;
import jonathan.modern_design.__config.utils.EnableTestContainers;
import jonathan.modern_design.banking.BankingAcceptanceConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static jonathan.modern_design.banking.domain.AccountDsl.DEFAULT_ACCOUNT_NUMBER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AcceptanceITRunner
@EnableTestContainers
class FindAccountAcceptanceIT extends BankingAcceptanceConfig {

    @Test
    void should_find_account_by_number() throws Exception {
        givenAnEmptyAccount();
        givenARandomAccountWithBalance(10.0);

        mockMvc.perform(get("/banking/v1/accounts/" + DEFAULT_ACCOUNT_NUMBER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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
