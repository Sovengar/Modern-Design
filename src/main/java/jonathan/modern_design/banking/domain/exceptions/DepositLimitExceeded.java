package jonathan.modern_design.banking.domain.exceptions;

import java.io.Serial;

public final class DepositLimitExceeded extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1450858818814932050L;
    private final String accountId;
    private final long attemptedAmount;
    private final long dailyLimit;

    public DepositLimitExceeded(String accountId, long attemptedAmount, long dailyLimit) {
        super("Deposit amount exceeds the daily limit");
        this.accountId = accountId;
        this.attemptedAmount = attemptedAmount;
        this.dailyLimit = dailyLimit;
    }

    public String accountId() {
        return accountId;
    }

    public long attemptedAmount() {
        return attemptedAmount;
    }

    public long dailyLimit() {
        return dailyLimit;
    }
}
