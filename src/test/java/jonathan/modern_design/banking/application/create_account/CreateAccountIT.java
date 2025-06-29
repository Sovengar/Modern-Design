package jonathan.modern_design.banking.application.create_account;

import com.fasterxml.jackson.databind.ObjectMapper;
import jonathan.modern_design.__config.shared_for_all_classes.AceptanceTest;
import jonathan.modern_design.__config.shared_for_all_classes.EnableTestContainers;
import jonathan.modern_design._dsl.AccountStub;
import jonathan.modern_design.auth.api.AuthApi;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static jonathan.modern_design._shared.domain.Currency.EUR;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//TODO ERROR WITH FEIGN @ApplicationModuleTest //Better than @SpringBootTest when using modules
@SpringBootTest
@AceptanceTest
@EnableTestContainers
class CreateAccountIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private AuthApi authApi;

    @Nested
    class WithValidDataForCreatingAccountShould {
        @Test
        void create_account() throws Exception {
            String json = mapper.writeValueAsString(AccountStub.CreateAccountMother.createAccountRequest(EUR.getCode()));
            //Use jsonPath("$.starships") ?

            mockMvc.perform(post("/v1/accounts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isCreated());
        }

        @Test
        void create_account_again() throws Exception {
            //Validate that @Rollback works
            String json = mapper.writeValueAsString(AccountStub.CreateAccountMother.createAccountRequest(EUR.getCode()));
            mockMvc.perform(post("/v1/accounts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isCreated());
        }
    }
}
