package jonathan.modern_design.account_module.domain.services;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design.account_module.domain.models.account.Account;
import jonathan.modern_design.account_module.domain.exceptions.AccountIsInactiveException;

@Injectable
public class AccountValidator {

    public void validateAccount(Account account) {
        validateActive(account);
    }

    private void validateActive(Account account) {
        if (!account.active()) {
            throw new AccountIsInactiveException(account.accountAccountNumber().accountNumber());
        }
    }
}

