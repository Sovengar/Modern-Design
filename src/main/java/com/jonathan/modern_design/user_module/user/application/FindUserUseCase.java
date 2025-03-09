package com.jonathan.modern_design.user_module.user.application;

import com.jonathan.modern_design.user_module.user.domain.model.User;

public interface FindUserUseCase {
    UserResource findUser(User.ID id);
}
