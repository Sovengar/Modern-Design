package com.jonathan.modern_design.user_module.application;

import com.jonathan.modern_design.user_module.domain.model.User;

import java.util.Map;

public record UserResource(String realname, String email, String country, String username, Map<String, String> deprecations) {

    public UserResource(User user) {
        this(user.getRealname().getName(), user.getEmail().getEmail(), user.getCountry().name(), user.getUsername().getName(), Map.of("name", "realname"));
    }

    public static UserResource from(final User user) {
        return new UserResource(user);
    }
}
