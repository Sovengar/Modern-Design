package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design._infra.config.annotations.BeanClass;
import com.jonathan.modern_design.user_module.user.application.UserRegister;
import com.jonathan.modern_design.user_module.user.domain.UserRepo;
import com.jonathan.modern_design.user_module.user.domain.model.User.UserId;
import com.jonathan.modern_design.user_module.user.dtos.UserRegisterCommand;
import com.jonathan.modern_design.user_module.user.dtos.UserResource;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@BeanClass
@RequiredArgsConstructor
class UserFacade implements UserApi {
    private final UserRepo userRepo;
    private final UserRegister userRegister;

    @Transactional
    public void registerUser(UserRegisterCommand command) {
        userRegister.registerUser(command);
    }

    public UserResource findUser(UserId userId) {
        final var user = userRepo.findByUUIDOrElseThrow(userId);
        return UserResource.from(user);
    }
}
