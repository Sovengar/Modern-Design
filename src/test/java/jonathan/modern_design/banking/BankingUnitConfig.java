package jonathan.modern_design.banking;

import jonathan.modern_design.__config.shared_for_all_classes.UnitTest;
import jonathan.modern_design.auth.api.AuthApi;
import jonathan.modern_design.banking.api.BankingApi;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import jonathan.modern_design.banking.infra.AccountingConfig;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@UnitTest
public class BankingUnitConfig {
    private final AccountingConfig accountingConfig = new AccountingConfig();
    protected final AccountRepo accountRepo = accountingConfig.getAccountRepo();
    @MockitoBean
    private AuthApi authApi;
    protected final BankingApi bankingApi = accountingConfig.accountApi(authApi);
}
