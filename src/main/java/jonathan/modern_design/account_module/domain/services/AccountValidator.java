package jonathan.modern_design.account_module.domain.services;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.exceptions.AccountIsInactiveException;

@Injectable
public class AccountValidator {

    public void validateAccount(Account account) {
        validateActive(account);
    }

    private void validateActive(Account account) {
        if (!account.isActive()) {
            throw new AccountIsInactiveException(account.getAccountNumber().accountNumber());
        }
    }
}

