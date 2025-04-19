package jonathan.modern_design.account_module.domain.repos;

import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;

public interface AccountRepo extends FindAccountRepo {
    AccountAccountNumber create(Account account);

    void update(Account account);

    void delete(final String accountNumber);

    void softDelete(final String accountNumber);

}
