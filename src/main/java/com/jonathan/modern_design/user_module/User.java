package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.user_module.vo.UserEmailVO;
import com.jonathan.modern_design.user_module.vo.UserNameVO;
import com.jonathan.modern_design.user_module.vo.UserPasswordVO;
import com.jonathan.modern_design.user_module.vo.UserRealNameVO;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class User {
    private UUID uuid;
    private UserRealNameVO realname;
    private UserNameVO username;
    private UserEmailVO email;
    private UserPasswordVO password;
    private String country;

    void setUuid(final UUID uuid) {
        this.uuid = uuid;
    }
}
