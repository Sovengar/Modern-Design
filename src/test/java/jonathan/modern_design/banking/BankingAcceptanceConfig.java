package jonathan.modern_design.banking;

import jonathan.modern_design.auth.api.AuthApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

public class BankingAcceptanceConfig extends BankingDsl {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ResourceLoader resourceLoader;

    @MockitoBean
    protected AuthApi authApi;
}
