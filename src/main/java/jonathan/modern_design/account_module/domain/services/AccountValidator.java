package jonathan.modern_design.account_module.domain.services;

import jonathan.modern_design._internal.config.annotations.Inyectable;
import jonathan.modern_design.account_module.domain.exceptions.AccountIsInactiveException;
import jonathan.modern_design.account_module.domain.model.Account;

@Inyectable
public class AccountValidator {

    public void validateAccount(Account account) {
        validateActive(account);
    }

    private void validateActive(Account account) {
        if (!account.isActive()) {
            throw new AccountIsInactiveException(account.getAccountNumber().getValue());
        }
    }
}

