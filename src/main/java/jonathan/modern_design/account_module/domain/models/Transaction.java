package jonathan.modern_design.account_module.domain.models;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jonathan.modern_design._common.annotations.AggregateRoot;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountMoney;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@AggregateRoot
@Entity
@Table(name = "transactions", schema = "md")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE) //For Hibernate
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    @Embeddable
    @Value //Not a record for Hibernate
    @NoArgsConstructor(force = true) //For Hibernate
    //@AllArgsConstructor
    public static class Id implements Serializable {
        @Serial private static final long serialVersionUID = 8283338134388675524L;
        String transactionId;

        public Id(UUID transactionId, TransactionType transactionType) {
            this.transactionId = transactionType.toString() + " - " + transactionId.toString();
        }
    }

    public static class Factory {
        private Factory() {
        }

        public static Transaction deposit(AccountMoney money, String destination) {
            return new Transaction(new Id(UUID.randomUUID(), TransactionType.DEPOSIT), LocalDateTime.now(), money, TransactionType.DEPOSIT, null, destination);
        }

        public static Transaction withdrawal(AccountMoney money, String origin) {
            return new Transaction(new Id(UUID.randomUUID(), TransactionType.WITHDRAWAL), LocalDateTime.now(), money, TransactionType.WITHDRAWAL, origin, null);
        }

        public static Transaction transfer(AccountMoney money, String origin, String destination) {
            return new Transaction(new Id(UUID.randomUUID(), TransactionType.TRANSFER), LocalDateTime.now(), money, TransactionType.TRANSFER, origin, destination);
        }
    }
}
