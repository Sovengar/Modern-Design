package jonathan.modern_design.banking.application.create_account;

import jonathan.modern_design.auth.api.AuthApi;
import jonathan.modern_design.banking.api.BankingApi;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import jonathan.modern_design.banking.infra.AccountingConfig;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class CreateAccountTest {
    private final AccountingConfig accountingConfig = new AccountingConfig();
    final AccountRepo accountRepo = accountingConfig.getAccountRepo();
    CreateAccount a = new CreateAccount(null, null, null, null, null);
    @MockitoBean
    private AuthApi authApi;
    private final BankingApi bankingApi = accountingConfig.accountApi(authApi);

    @Test
    void should_create_account_successfully() {
        //Testing package method
        a.complexLogicHere();
    }
}
