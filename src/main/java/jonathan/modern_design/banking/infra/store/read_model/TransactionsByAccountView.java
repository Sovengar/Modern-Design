package jonathan.modern_design.banking.infra.store.read_model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions_by_account_view", schema = "banking")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TransactionsByAccountView {
    @Id
    private String transactionId;
    private LocalDateTime transactionDate;
    private BigDecimal balance;
    private String currency;
    private String transactionType;
    private String originAccount;
    private String destinationAccount;
}
