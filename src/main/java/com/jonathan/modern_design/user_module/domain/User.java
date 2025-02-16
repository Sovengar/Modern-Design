package com.jonathan.modern_design.user_module.domain;

import com.jonathan.modern_design.user_module.model.UserEmail;
import com.jonathan.modern_design.user_module.model.UserName;
import com.jonathan.modern_design.user_module.model.UserPassword;
import com.jonathan.modern_design.user_module.model.UserRealName;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class User {
    private Long id;
    private UUID uuid;
    private UserRealName realname;
    private UserName username;
    private UserEmail email;
    private UserPassword password;
    private String country;
}
