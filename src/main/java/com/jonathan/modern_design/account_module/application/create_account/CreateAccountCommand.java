package com.jonathan.modern_design.account_module.application.create_account;

import lombok.Builder;

@Builder
public record CreateAccountCommand(String realname, String email, String username, String address, String password, String country,
                                   String currency) {
}
