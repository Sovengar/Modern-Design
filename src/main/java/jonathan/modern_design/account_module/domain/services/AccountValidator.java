package jonathan.modern_design.account_module.domain.services;

import jonathan.modern_design._shared.tags.DomainService;
import jonathan.modern_design.account_module.domain.exceptions.AccountIsInactiveException;
import jonathan.modern_design.account_module.domain.models.account.Account;

@DomainService
public class AccountValidator {

    public void validateAccount(Account account) {
        validateActive(account);
    }

    private void validateActive(Account account) {
        if (account.getStatus() != Account.Status.ACTIVE) {
            throw new AccountIsInactiveException(account.getAccountNumber().getAccountNumber());
        }
    }
}

