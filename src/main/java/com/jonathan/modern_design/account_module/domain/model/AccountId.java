package com.jonathan.modern_design.account_module.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Value;

@Value //Not a record because ORM needs mutability
@Embeddable
public class AccountId {
    Long accountId;
}
