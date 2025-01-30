package com.jonathan.modern_design.user_module.application;

import com.jonathan.modern_design.common.BeanClass;
import com.jonathan.modern_design.user_module.application.create_user.CreateUserUseCase;
import com.jonathan.modern_design.user_module.domain.model.User;
import lombok.RequiredArgsConstructor;

@BeanClass
@RequiredArgsConstructor
public class UserFacade implements CreateUserUseCase {
    @Override
    public User createUser() {
        return null; //TODO
    }
}
