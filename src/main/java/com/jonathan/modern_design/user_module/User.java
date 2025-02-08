package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.user_module.vo.UserEmailVO;
import com.jonathan.modern_design.user_module.vo.UserNameVO;
import com.jonathan.modern_design.user_module.vo.UserPasswordVO;
import com.jonathan.modern_design.user_module.vo.UserRealNameVO;
import lombok.Builder;

import java.util.UUID;

@Builder
public class User {

    @Builder.Default
    private UUID uuid = UUID.randomUUID();
    private UserRealNameVO realname;
    private UserNameVO username;
    private UserEmailVO email;
    private UserPasswordVO password;
    private String country;

    public UUID getId() {
        return uuid;
    }
}
