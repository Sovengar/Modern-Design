package jonathan.modern_design.banking.api;

import jonathan.modern_design.banking.api.dtos.AccountDto;

import java.util.UUID;

public interface AccountQueryApi {
    AccountDto findOne(final String accountNumber);

    AccountDto findByUserId(UUID userId);
}

