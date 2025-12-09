package jonathan.modern_design._shared.api.events.banking;

import jonathan.modern_design._shared.domain.vo.Money;
import org.springframework.modulith.events.Externalized;

import java.util.UUID;

//CRUD event, public/integration style, event-carried state transfer, very high coupling
@Externalized
public record AccountSnapshot(String accountNumber, Money money, String status, UUID accountHolderId) {
}
