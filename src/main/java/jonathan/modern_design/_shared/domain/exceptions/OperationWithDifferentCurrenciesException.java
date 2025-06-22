package jonathan.modern_design._shared.domain.exceptions;

import jonathan.modern_design._shared.infra.config.exception.RootException;

import java.io.Serial;

public class OperationWithDifferentCurrenciesException extends RootException {
    @Serial private static final long serialVersionUID = 8611756717306849952L;

    public OperationWithDifferentCurrenciesException() {
        super("Operation with different currencies");
    }
}
