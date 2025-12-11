package jonathan.modern_design.banking.domain;

import jonathan.modern_design.banking.domain.models.AccountHolder;

import java.util.Optional;
import java.util.UUID;

public class AccountHolderDsl {
    public static AccountHolder randomAccountHolder() {
        return accountHolder(UUID.randomUUID(), UUID.randomUUID());
    }

    public static AccountHolder accountHolder(UUID accountHolderId, UUID userId) {
        return AccountHolder.create(accountHolderId, Optional.of(AccountDsl.fullName), AccountDsl.personalId, AccountDsl.ahAddress, AccountDsl.birthdate, AccountDsl.phoneNumbers, userId);
    }
}
