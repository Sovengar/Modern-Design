package jonathan.modern_design.banking.domain.exceptions;

import jonathan.modern_design._shared.config.exception.RootException;

import java.io.Serial;

public class AccountNotFoundException extends RootException {

    @Serial private static final long serialVersionUID = -3171077750682245887L;

    public AccountNotFoundException(String accountNumber) {
        super(String.format("Account with ID %s not found!", accountNumber));
    }
}
