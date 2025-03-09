package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design._infra.config.annotations.BeanClass;
import com.jonathan.modern_design.user_module.user.application.FindUserUseCase;
import com.jonathan.modern_design.user_module.user.application.RegisterUserUseCase;
import com.jonathan.modern_design.user_module.user.application.UserResource;
import com.jonathan.modern_design.user_module.user.domain.UserRepo;
import com.jonathan.modern_design.user_module.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@BeanClass
@RequiredArgsConstructor
@Transactional
public class UserFacade implements RegisterUserUseCase, FindUserUseCase {
    private final UserRepo userRepo;
    private final RegisterUserUseCase registerUserUseCase;

    @Override
    public User.ID registerUser(RegisterUserCommand command) {
        return registerUserUseCase.registerUser(command);
    }

    @Override
    public UserResource findUser(User.ID id) {
        final var user = userRepo.findByUUIDOrElseThrow(id);
        return UserResource.from(user);
    }
}
