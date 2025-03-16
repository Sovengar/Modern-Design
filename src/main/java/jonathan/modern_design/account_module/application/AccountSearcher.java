package jonathan.modern_design.account_module.application;

import jonathan.modern_design.account_module.dtos.AccountResource;
import jonathan.modern_design.account_module.infra.AccountSearchRepo;
import lombok.Builder;

import java.util.List;

public interface AccountSearcher extends AccountSearchRepo {

    List<AccountResource> search(AccountSearchCriteria filters);

    @Builder
    record AccountSearchCriteria(
            String username,
            String email,
            String countryCode
    ) {
    }
}
