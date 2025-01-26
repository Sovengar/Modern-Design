package com.jonathan.modern_design.account.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountId implements Serializable {

    private final Long value;

    public static AccountId of(Long id) {
        return new AccountId(id);
    }

}
