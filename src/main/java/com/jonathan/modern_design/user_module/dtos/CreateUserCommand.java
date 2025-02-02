package com.jonathan.modern_design.user_module.dtos;

public record CreateUserCommand(String name, String email, String firstname, String lastname, String password, String country) {
}
