package jonathan.modern_design.banking.api;

import jonathan.modern_design.banking.api.dtos.AccountDto;

public interface AccountQueryApi {
    AccountDto findOne(final String accountNumber);
}

