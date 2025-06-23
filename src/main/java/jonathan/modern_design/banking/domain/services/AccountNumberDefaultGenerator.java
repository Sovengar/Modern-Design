package jonathan.modern_design.banking.domain.services;

import jonathan.modern_design._shared.tags.DomainService;
import jonathan.modern_design.banking.domain.models.Account;
import jonathan.modern_design.banking.domain.policies.AccountNumberGenerator;

import java.util.UUID;

@DomainService
public class AccountNumberDefaultGenerator implements AccountNumberGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String generate(Account account) {
        return UUID.randomUUID().toString();
    }
}
