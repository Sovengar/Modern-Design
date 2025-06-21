package jonathan.modern_design.banking.domain.policies;

import jonathan.modern_design.banking.domain.models.Account;

public interface AccountNumberGenerator {
    String generate();

    String generate(Account account);
}
