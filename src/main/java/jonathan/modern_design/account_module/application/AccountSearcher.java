package jonathan.modern_design.account_module.application;

import jonathan.modern_design.account_module.dtos.AccountResource;
import lombok.Builder;

import java.util.List;

public interface AccountSearcher {

    List<AccountResource> search(AccountSearchCriteria filters);

    @Builder
    record AccountSearchCriteria(
            String username,
            String email,
            String countryCode
    ) {
    }
}
