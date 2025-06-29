package jonathan.modern_design.banking.application.deposit;

import jonathan.modern_design.__config.shared_for_all_classes.AceptanceTest;
import jonathan.modern_design.__config.shared_for_all_classes.EnableTestContainers;
import jonathan.modern_design.banking.api.BankingApi;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static jonathan.modern_design._dsl.AccountStub.CreateAccountMother.createAccountCommand;
import static jonathan.modern_design._shared.domain.Currency.EUR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//TODO ERROR WITH FEIGN @ApplicationModuleTest //Better than @SpringBootTest when using modules
@SpringBootTest
@AceptanceTest
@EnableTestContainers
class DepositIT {
    @Autowired
    private BankingApi bankingApi;

    @Autowired
    private AccountRepo repository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_deposit_funds_via_http_request() throws Exception {
        var accountNumber = bankingApi.createAccount(createAccountCommand(EUR.getCode())).getAccountNumber();

        // Act
        mockMvc.perform(put("/v1/accounts/" + accountNumber + "/deposit/100/EUR"))
                .andExpect(status().isOk());

        // Assert
        var updated = repository.findByAccNumber(accountNumber).orElseThrow();
        assertEquals(BigDecimal.valueOf(100), updated.getMoney().getBalance());
    }
}
