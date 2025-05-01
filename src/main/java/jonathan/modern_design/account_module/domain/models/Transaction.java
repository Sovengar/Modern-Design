package jonathan.modern_design.account_module.domain.models;

import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jonathan.modern_design._common.annotations.AggregateRoot;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountMoney;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "transactions", schema = "md")
@Getter
@NoArgsConstructor(access = PRIVATE) //For Hibernate
@AllArgsConstructor(access = PRIVATE)
@AggregateRoot
public class Transaction {
    @EmbeddedId
    private Id transactionId;
    private LocalDateTime transactionDate;
    @Embedded
    private AccountMoney money;
    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    private TransactionType transactionType;
    private String origin;
    private String destination;

    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER
    }

    @Value //Not a record for Hibernate
    @NoArgsConstructor(access = PRIVATE, force = true) //For Hibernate
    @AllArgsConstructor(access = PRIVATE)
    public static class Id implements Serializable {
        @Serial private static final long serialVersionUID = 8283338134388675524L;
        String transactionId;

        public static Id of(UUID transactionId, TransactionType transactionType) {
            return new Id(transactionType.toString() + " - " + transactionId.toString());
        }
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class Factory {
        public static Transaction deposit(AccountMoney money, String destination) {
            return new Transaction(Id.of(UUID.randomUUID(), TransactionType.DEPOSIT), LocalDateTime.now(), money, TransactionType.DEPOSIT, null, destination);
        }

        public static Transaction withdrawal(AccountMoney money, String origin) {
            return new Transaction(Id.of(UUID.randomUUID(), TransactionType.WITHDRAWAL), LocalDateTime.now(), money, TransactionType.WITHDRAWAL, origin, null);
        }

        public static Transaction transfer(AccountMoney money, String origin, String destination) {
            return new Transaction(Id.of(UUID.randomUUID(), TransactionType.TRANSFER), LocalDateTime.now(), money, TransactionType.TRANSFER, origin, destination);
        }
    }
}
