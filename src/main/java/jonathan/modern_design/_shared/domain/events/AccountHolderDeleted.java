package jonathan.modern_design._shared.domain.events;

import java.util.UUID;

public record AccountHolderDeleted(UUID accountHolderId, UUID userId) {
}
