package jonathan.modern_design.banking.api.events;

import java.util.UUID;

public record AccountHolderDeleted(UUID accountHolderId, UUID userId) {
}
