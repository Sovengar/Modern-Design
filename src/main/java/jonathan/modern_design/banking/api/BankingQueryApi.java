package jonathan.modern_design.banking.api;

import jonathan.modern_design.banking.api.dtos.AccountDto;

import java.util.Optional;
import java.util.UUID;

public interface BankingQueryApi {
    Optional<AccountDto> findOne(final String accountNumber);

    Optional<AccountDto> findByUserId(UUID userId);
}

