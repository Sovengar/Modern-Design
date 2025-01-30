package com.jonathan.modern_design.user_module.application.create_user;

public record CreateUserCommand(String name, String email, String firstname, String lastname, String password, String country) {
}
