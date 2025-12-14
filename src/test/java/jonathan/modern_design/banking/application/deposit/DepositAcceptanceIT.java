package jonathan.modern_design.banking.application.deposit;

import jonathan.modern_design.__config.runners.AcceptanceITRunner;
import jonathan.modern_design.__config.utils.EnableTestContainers;
import jonathan.modern_design.banking.BankingAcceptanceConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static jonathan.modern_design.banking.domain.AccountDsl.DEFAULT_ACCOUNT_NUMBER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//TODO ERROR WITH FEIGN @ApplicationModuleTest //Better than @SpringBootTest when using modules
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AcceptanceITRunner
@EnableTestContainers
class DepositAcceptanceIT extends BankingAcceptanceConfig {
    @Test
    void should_deposit_funds_via_http_request() throws Exception {
        givenAnEmptyAccount();

        mockMvc.perform(put("/banking/v1/accounts/" + DEFAULT_ACCOUNT_NUMBER + "/deposit/10/EUR"))
                .andExpect(status().isOk());
    }
}
