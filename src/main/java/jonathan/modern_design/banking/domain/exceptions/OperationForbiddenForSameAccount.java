package jonathan.modern_design.banking.domain.exceptions;

import jonathan.modern_design._config.exception.RootException;

import java.io.Serial;

public class OperationForbiddenForSameAccount extends RootException {
    @Serial private static final long serialVersionUID = 809998136604950305L;

    public OperationForbiddenForSameAccount() {
        super("Operation forbidden for same account");
    }
}
