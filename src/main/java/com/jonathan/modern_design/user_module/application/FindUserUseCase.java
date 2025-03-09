package com.jonathan.modern_design.user_module.application;

import com.jonathan.modern_design.user_module.domain.model.User;

public interface FindUserUseCase {
    UserResource findUser(User.ID id);
}
