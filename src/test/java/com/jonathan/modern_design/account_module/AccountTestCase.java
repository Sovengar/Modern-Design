package com.jonathan.modern_design.account_module;

import com.jonathan.modern_design.account_module.application.AccountFacade;
import com.jonathan.modern_design.config.IntegrationTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static com.jonathan.modern_design.fake_data.AccountDataStub.randomAccount;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountTestCase extends IntegrationTestConfig {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private AccountFacade accountFacade;


    @Test
    void should_create_account() throws Exception {
        //Given
        var accountCreated = accountFacade.createAccount(randomAccount());
        mockMvc.perform(post("/accounts")).andExpect(status().isOk()).andReturn();
        //assertThat().isNotNull();
        //TODO assertThat(accountFacade.createAccount()).isNotNull();
    }


}


