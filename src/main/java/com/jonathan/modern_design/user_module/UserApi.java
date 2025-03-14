package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.user_module.domain.User;
import com.jonathan.modern_design.user_module.dtos.UserRegisterCommand;
import com.jonathan.modern_design.user_module.dtos.UserResource;

public interface UserApi {
    void registerUser(UserRegisterCommand command);

    UserResource findUser(User.UserId userId);
}
