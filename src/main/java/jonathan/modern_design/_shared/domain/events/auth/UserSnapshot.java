package jonathan.modern_design._shared.domain.events.auth;

import java.util.UUID;

public record UserSnapshot(UUID userId, String username, String email) {
}
