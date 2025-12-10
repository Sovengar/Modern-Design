package jonathan.modern_design.banking.application.create_account;

import jonathan.modern_design.auth.api.AuthApi;
import jonathan.modern_design.banking.BankingUnitConfig;
import jonathan.modern_design.banking.api.BankingApi;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import jonathan.modern_design.banking.infra.AccountingConfig;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class CreateAccountTest extends BankingUnitConfig {
    private final AccountingConfig accountingConfig = new AccountingConfig();
    private final AccountRepo accountRepo = accountingConfig.getAccountRepo();
    private CreateAccount createAccount = new CreateAccount(null, null, null, null, null);
    @MockitoBean
    private AuthApi authApi;
    private final BankingApi bankingApi = accountingConfig.accountApi(authApi);

    @Test
    void should_create_account_successfully() {
        //Testing package method
        createAccount.complexLogicHere();
    }
}
