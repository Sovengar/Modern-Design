package jonathan.modern_design._shared.events.banking;

import jonathan.modern_design._shared.domain.vo.Money;
import jonathan.modern_design.banking.domain.models.Account;
import org.springframework.modulith.events.Externalized;

import java.util.UUID;

//CRUD event, public/integration style, event-carried state transfer, very high coupling
@Externalized
public record AccountSnapshot(String accountNumber, Money money, Account.Status status, UUID accountHolderId) {
}
