package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design._infra.config.annotations.BeanClass;
import com.jonathan.modern_design.user_module.user.application.UserRegister;
import com.jonathan.modern_design.user_module.user.domain.UserRepo;
import com.jonathan.modern_design.user_module.user.domain.model.User;
import com.jonathan.modern_design.user_module.user.dtos.UserRegisterCommand;
import com.jonathan.modern_design.user_module.user.dtos.UserResource;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@BeanClass
@RequiredArgsConstructor
public class UserFacade {
    private final UserRepo userRepo;
    private final UserRegister userRegister;

    @Transactional
    public User.ID registerUser(UserRegisterCommand command) {
        return userRegister.registerUser(command);
    }

    public UserResource findUser(User.ID id) {
        final var user = userRepo.findByUUIDOrElseThrow(id);
        return UserResource.from(user);
    }
}
