package com.jonathan.modern_design.account_module.application.create_account;

import lombok.Builder;

@Builder
public record AccountDataCommand (String name, String email, String firstname, String lastname, String password, String country) {
}
