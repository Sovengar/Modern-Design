package jonathan.modern_design.banking.domain.models;

import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jonathan.modern_design._shared.domain.vo.Money;
import jonathan.modern_design._shared.tags.persistence.InMemoryOnlyCatalog;
import jonathan.modern_design.banking.domain.events.TransactionRegistered;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "transactions", schema = "banking")
@Getter
@NoArgsConstructor(access = PRIVATE) //For Hibernate
public class Transaction extends AbstractAggregateRoot<Transaction> {
    @EmbeddedId
    private Id transactionId;

    private LocalDateTime transactionDate;

    @Embedded
    private Money money;

    @InMemoryOnlyCatalog
    @Enumerated(value = EnumType.STRING)
    private TransactionType transactionType;

    private String origin;
    private String destination;

    Transaction(Id transactionId, LocalDateTime transactionDate, Money money, TransactionType transactionType, String origin, String destination) {
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.money = money;
        this.transactionType = transactionType;
        this.origin = origin;
        this.destination = destination;

        this.registerEvent(new TransactionRegistered(transactionId.getTransactionId(), origin, destination));
    }

    @Getter
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
        public static Transaction deposit(Money money, String destination) {
            return new Transaction(Id.of(UUID.randomUUID(), TransactionType.DEPOSIT), LocalDateTime.now(), money, TransactionType.DEPOSIT, null, destination);
        }

        public static Transaction withdrawal(Money money, String origin) {
            return new Transaction(Id.of(UUID.randomUUID(), TransactionType.WITHDRAWAL), LocalDateTime.now(), money, TransactionType.WITHDRAWAL, origin, null);
        }

        public static Transaction transfer(Money money, String origin, String destination) {
            return new Transaction(Id.of(UUID.randomUUID(), TransactionType.TRANSFER), LocalDateTime.now(), money, TransactionType.TRANSFER, origin, destination);
        }
    }
}
