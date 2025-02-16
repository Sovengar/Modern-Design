package com.jonathan.modern_design._fake_data;

import com.jonathan.modern_design.user_module.domain.User;
import com.jonathan.modern_design.user_module.model.UserEmail;
import com.jonathan.modern_design.user_module.model.UserName;
import com.jonathan.modern_design.user_module.model.UserPassword;
import com.jonathan.modern_design.user_module.model.UserRealName;

import java.util.UUID;

public class UserStub extends Stub {
    public static final String DEFAULT_COUNTRY = "ES";
    public static final UUID DEFAULT_UUID = UUID.fromString("47611f29-731c-4dcc-966b-3537c35e8ace");

    public static User normalUser() {
        return User.builder()
                .uuid(DEFAULT_UUID)
                .realname(UserRealName.of(faker.name().fullName()))
                .username(UserName.of(faker.name().username()))
                .email(UserEmail.of(faker.internet().emailAddress()))
                .password(UserPassword.of(faker.internet().password()))
                .country(DEFAULT_COUNTRY)
                .build();
    }

}
