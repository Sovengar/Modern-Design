package jonathan.modern_design.banking.application.deposit;

import jonathan.modern_design.__config.shared_for_all_classes.AceptanceTest;
import jonathan.modern_design.__config.shared_for_all_classes.EnableTestContainers;
import jonathan.modern_design._dsl.BankingDsl;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static jonathan.modern_design._dsl.AccountStub.DEFAULT_ACCOUNT_NUMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//TODO ERROR WITH FEIGN @ApplicationModuleTest //Better than @SpringBootTest when using modules
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AceptanceTest
@EnableTestContainers
class DepositAcceptanceIT extends BankingDsl {
    @Autowired
    private AccountRepo repository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_deposit_funds_via_http_request() throws Exception {
        givenAnEmptyAccount();

        // Act
        mockMvc.perform(put("/banking/v1/accounts/" + DEFAULT_ACCOUNT_NUMBER + "/deposit/10/EUR"))
                .andExpect(status().isOk());

        // Assert
        var fetchedAccount = repository.findByAccNumberOrElseThrow(DEFAULT_ACCOUNT_NUMBER);
        assertEquals(BigDecimal.TEN, fetchedAccount.getMoney().getBalance());
    }
}
