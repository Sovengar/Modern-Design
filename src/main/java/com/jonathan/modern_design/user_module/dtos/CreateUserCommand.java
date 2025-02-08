package com.jonathan.modern_design.user_module.dtos;

import lombok.Builder;

@Builder
public record CreateUserCommand(String realname, String email, String username, String password, String country) {
}
