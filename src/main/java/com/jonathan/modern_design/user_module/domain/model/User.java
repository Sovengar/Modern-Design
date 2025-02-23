package com.jonathan.modern_design.user_module.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class User {
    private UserId id;
    private UUID uuid;
    private UserRealName realname;
    private UserName username;
    private UserEmail email;
    private UserPassword password;
    private String country;

    public record UserId(Long value) {
    }
}
