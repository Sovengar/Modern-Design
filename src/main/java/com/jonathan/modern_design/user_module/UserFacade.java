package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design._infra.config.annotations.Inyectable;
import com.jonathan.modern_design.user_module.application.UserRegister;
import com.jonathan.modern_design.user_module.domain.User.UserId;
import com.jonathan.modern_design.user_module.domain.UserRepo;
import com.jonathan.modern_design.user_module.dtos.UserRegisterCommand;
import com.jonathan.modern_design.user_module.dtos.UserResource;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Inyectable
@RequiredArgsConstructor
class UserFacade implements UserApi {
    private final UserRepo userRepo;
    private final UserRegister userRegister;

    @Override
    @Transactional
    public void registerUser(UserRegisterCommand command) {
        userRegister.registerUser(command);
    }

    @Override
    public UserResource findUser(UserId userId) {
        final var user = userRepo.findByUUIDOrElseThrow(userId);
        return UserResource.from(user);
    }
}
