package com.jonathan.modern_design.account_module.application.create_account;

import com.jonathan.modern_design.shared.Currency;
import lombok.Builder;

@Builder
public record CreateAccountCommand(String realname, String email, String username, String address, String password, String country,
                                   Currency currency) {
}
