package jonathan.modern_design.search.view_models;

import java.util.UUID;

public record AccountWithUserInfo(
        String accountNumber,
        UUID userId,
        String username,
        String email
) {

}
