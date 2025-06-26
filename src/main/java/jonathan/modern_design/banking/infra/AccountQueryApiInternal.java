package jonathan.modern_design.banking.infra;

import jonathan.modern_design._shared.tags.Facade;
import jonathan.modern_design.banking.api.AccountQueryApi;
import jonathan.modern_design.banking.api.dtos.AccountDto;
import jonathan.modern_design.banking.queries.FindAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Facade
@RequiredArgsConstructor
@Slf4j
class AccountQueryApiInternal implements AccountQueryApi {
    private final FindAccount findAccount;

    @Override
    public AccountDto findOne(final String accountNumber) {
        return findAccount.queryWith(accountNumber);
    }
}
