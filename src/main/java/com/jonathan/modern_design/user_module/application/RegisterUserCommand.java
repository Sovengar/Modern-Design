package com.jonathan.modern_design.user_module.application;

import lombok.Builder;

import java.util.UUID;

@Builder
public record RegisterUserCommand(UUID uuid, String realname, String email, String username, String password, String country) {
}
