package jonathan.modern_design.account_module.domain.store;

import jonathan.modern_design.account_module.domain.models.Transaction;

public interface TransactionRepo {
    void create(final Transaction transaction);
}
