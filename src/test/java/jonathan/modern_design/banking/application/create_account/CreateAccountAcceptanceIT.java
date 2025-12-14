package jonathan.modern_design.banking.application.create_account;

import jonathan.modern_design.__config.runners.AcceptanceITRunner;
import jonathan.modern_design.__config.utils.EnableTestContainers;
import jonathan.modern_design.banking.BankingAcceptanceConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//TODO ERROR WITH FEIGN @ApplicationModuleTest //Better than @SpringBootTest when using modules
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AcceptanceITRunner
@EnableTestContainers
class CreateAccountAcceptanceIT extends BankingAcceptanceConfig {
    @Test
    void should_create_account() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:./requests/banking/create-account.json");
        String json = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        mockMvc.perform(post("/banking/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
        //.andExpect(jsonPath("$.accountNumber").exists())
        ;
    }
}
