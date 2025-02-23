package com.jonathan.modern_design.user_module.infra;

import com.jonathan.modern_design.user_module.domain.model.User;

import java.util.Map;

public record UserResource(String realname, String email, String country, String username, Map<String, String> deprecations) {

    public UserResource(User user) {
        this(user.getRealname().getName(), user.getEmail().getEmail(), user.getCountry(), user.getUsername().getName(), Map.of("name", "realname"));
    }
}
