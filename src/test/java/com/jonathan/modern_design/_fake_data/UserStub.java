package com.jonathan.modern_design._fake_data;

import com.jonathan.modern_design.user_module.User;
import com.jonathan.modern_design.user_module.vo.UserEmailVO;
import com.jonathan.modern_design.user_module.vo.UserNameVO;
import com.jonathan.modern_design.user_module.vo.UserPasswordVO;
import com.jonathan.modern_design.user_module.vo.UserRealNameVO;

import java.util.UUID;

public class UserStub extends Stub {
    public static final String DEFAULT_COUNTRY = "ES";
    private static final UUID DEFAULT_ID = UUID.randomUUID();

    public static User normalUser() {
        return User.builder()
                .uuid(DEFAULT_ID)
                .realname(UserRealNameVO.of(faker.name().fullName()))
                .username(UserNameVO.of(faker.name().username()))
                .email(UserEmailVO.of(faker.internet().emailAddress()))
                .password(UserPasswordVO.of(faker.internet().password()))
                .country(DEFAULT_COUNTRY)
                .build();
    }

}
