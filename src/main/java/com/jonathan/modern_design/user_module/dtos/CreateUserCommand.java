package com.jonathan.modern_design.user_module.dtos;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateUserCommand(UUID uuid, String realname, String email, String username, String password, String country) {
}
