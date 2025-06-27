package jonathan.modern_design.search.view_models;

import java.math.BigDecimal;

public record AccountWithUserInfo(
        String accountNumber,
        BigDecimal balance,
        String username,
        String email
) {

}
