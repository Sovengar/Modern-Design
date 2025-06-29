package jonathan.modern_design.banking.domain.exceptions;

import jonathan.modern_design._config.exception.RootException;

import java.io.Serial;

public class AccountIsAlreadyActiveException extends RootException {
    @Serial private static final long serialVersionUID = 2772743614826379676L;

    public AccountIsAlreadyActiveException(String accountNumber) {
        super(String.format("Account with number %s is already active.", accountNumber));
    }
}
