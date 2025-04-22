package jonathan.modern_design.account_module.domain.models;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jonathan.modern_design._common.AuditingColumns;
import jonathan.modern_design._common.annotations.AggregateRoot;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountMoney;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.hibernate.annotations.SQLRestriction;

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
@SQLRestriction("deleted <> true") //Make Hibernate ignore soft deleted entries
public class Transaction extends AuditingColumns {
    @EmbeddedId
    Id transactionId;
    LocalDateTime transactionDate;
    @Embedded
    AccountMoney money;
    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    TransactionType transactionType;
    String origin;
    String destination;

    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER
    }

    @Embeddable
    @Value //Not a record for Hibernate
    @NoArgsConstructor(force = true) //For Hibernate
    @AllArgsConstructor
    public static class Id implements Serializable {
        @Serial private static final long serialVersionUID = 8283338134388675524L;
        UUID transactionId;
    }

    public static class Factory {
        private Factory() {
        }

        public static Transaction deposit(AccountMoney money, String destination) {
            return new Transaction(new Id(UUID.randomUUID()), LocalDateTime.now(), money, TransactionType.DEPOSIT, null, destination);
        }

        public static Transaction withdrawal(AccountMoney money, String origin) {
            return new Transaction(new Id(UUID.randomUUID()), LocalDateTime.now(), money, TransactionType.WITHDRAWAL, origin, null);
        }

        public static Transaction transfer(AccountMoney money, String origin, String destination) {
            return new Transaction(new Id(UUID.randomUUID()), LocalDateTime.now(), money, TransactionType.TRANSFER, origin, destination);
        }
    }
}
