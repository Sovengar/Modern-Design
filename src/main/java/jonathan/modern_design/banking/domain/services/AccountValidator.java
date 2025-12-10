package jonathan.modern_design.banking.domain.services;

import jonathan.modern_design._shared.tags.DomainService;
import jonathan.modern_design.banking.domain.exceptions.AccountIsInactiveException;
import jonathan.modern_design.banking.domain.models.Account;

@DomainService
public class AccountValidator {

    public void validateAccount(Account account) {
        validateActive(account);
    }

    private void validateActive(Account account) {
        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new AccountIsInactiveException(account.getAccountNumber());
        }
    }
}

