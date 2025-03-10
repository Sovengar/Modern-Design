package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.user_module.user.domain.model.User;
import com.jonathan.modern_design.user_module.user.dtos.UserRegisterCommand;
import com.jonathan.modern_design.user_module.user.dtos.UserResource;

public interface UserApi {
    void registerUser(UserRegisterCommand command);

    UserResource findUser(User.UserId userId);
}
