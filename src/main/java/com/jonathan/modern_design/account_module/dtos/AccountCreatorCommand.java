package com.jonathan.modern_design.account_module.dtos;

public record AccountCreatorCommand(String realname, String email, String username, String address, String password, String country,
                                    String currency) {
}
