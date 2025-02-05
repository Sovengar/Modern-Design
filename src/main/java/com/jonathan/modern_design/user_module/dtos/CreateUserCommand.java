package com.jonathan.modern_design.user_module.dtos;

import lombok.Builder;

@Builder
public record CreateUserCommand(String name, String email, String firstname, String lastname, String password, String country) {
}
