package jonathan.modern_design.account_module.domain.exceptions;

import jonathan.modern_design._internal.config.exception.RootException;

import java.io.Serial;

public class AccountIsInactiveException extends RootException {
    @Serial private static final long serialVersionUID = -5633038512235383839L;

    public AccountIsInactiveException(String accountNumber) {
        super(String.format("Account %s is inactive", accountNumber));
    }
}
