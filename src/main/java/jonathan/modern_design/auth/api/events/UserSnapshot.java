package jonathan.modern_design.auth.api.events;

import java.util.UUID;

public record UserSnapshot(UUID userId, String username, String email) {
}
