package com.jonathan.modern_design.account_module.infra.query;

import lombok.Builder;

@Builder // For tests
public record AccountSearchCriteria(
        String username,
        String email,
        String countryCode
) {
}
