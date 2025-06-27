package jonathan.modern_design.banking.domain.events;

public record TransactionRegistered(String transactionId, String sourceAccountNumber, String targetAccountNumber) {
}
