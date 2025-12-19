package jonathan.modern_design.banking.application.create_account;

import jonathan.modern_design.__config.initializers.InfraInitializer;
import jonathan.modern_design.__config.runners.AcceptanceRunner;
import jonathan.modern_design.banking.BankingAcceptanceConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ApplicationModuleTest //Better than @SpringBootTest when using modules
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AcceptanceRunner
@InfraInitializer
class CreateAccountAT extends BankingAcceptanceConfig {
    @Test
    void should_create_account() throws Exception {
        mockMvc.perform(post("/banking/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loader.loadJsonFromClasspath("./requests/banking/create-account.json")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").exists());
    }
}
