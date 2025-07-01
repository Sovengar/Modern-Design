package jonathan.modern_design._shared.events.banking;

import java.util.UUID;

public record AccountHolderDeleted(UUID accountHolderId, UUID userId) {
}
