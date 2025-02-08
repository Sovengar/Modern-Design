package com.jonathan.modern_design.account_module.domain.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountIdVO implements Serializable {

    private final Long value;

    public static AccountIdVO of(Long id) {
        return new AccountIdVO(id);
    }

}
