package jonathan.modern_design.account_module.application.deposit;

import jonathan.modern_design.__config.ITConfig;
import jonathan.modern_design.account_module.api.AccountApi;
import jonathan.modern_design.account_module.domain.store.AccountRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static jonathan.modern_design._fake_data.AccountStub.CreateAccountMother.randomAccountWithCurrency;
import static jonathan.modern_design._shared.Currency.EUR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DepositHttpControllerIT extends ITConfig {
    @Autowired
    private AccountApi accountFacade;
    @Autowired
    private AccountRepo repository;

    @Test
    void should_deposit_funds_via_http_request() throws Exception {
        var accountNumber = accountFacade.createAccount(randomAccountWithCurrency(EUR)).getAccountNumber();

        // Act
        mockMvc.perform(put("/api/v1/accounts/" + accountNumber + "/deposit/100/EUR"))
                .andExpect(status().isOk());

        // Assert
        var updated = repository.findByAccNumber(accountNumber).orElseThrow();
        assertEquals(BigDecimal.valueOf(100), updated.getMoney().getBalance());
    }
}
