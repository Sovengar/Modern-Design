package com.jonathan.modern_design.user_module.dtos;

import com.jonathan.modern_design.user_module.domain.User;

import java.util.Map;

public record UserResource(String realname, String email, String country, String username, Map<String, String> deprecations) {

    private UserResource(User user) {
        this(user.getRealname().getRealname().orElse(""), user.getEmail().getValue(), user.getCountry(), user.getUsername().getUsername(), Map.of("name", "realname"));
    }

    public static UserResource from(final User user) {
        return new UserResource(user);
    }
}
