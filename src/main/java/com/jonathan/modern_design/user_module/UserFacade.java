package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design._infra.config.annotations.BeanClass;
import com.jonathan.modern_design.user_module.application.FindUserUseCase;
import com.jonathan.modern_design.user_module.application.RegisterUserUseCase;
import com.jonathan.modern_design.user_module.application.UserResource;
import com.jonathan.modern_design.user_module.domain.UserRepository;
import com.jonathan.modern_design.user_module.domain.model.UserIdentifiers;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@BeanClass
@RequiredArgsConstructor
@Transactional
public class UserFacade implements RegisterUserUseCase, FindUserUseCase {
    private final UserRepository userRepository;
    private final RegisterUserUseCase registerUserUseCase;

    @Override
    public UserIdentifiers registerUser(RegisterUserCommand command) {
        return registerUserUseCase.registerUser(command);
    }

    @Override
    public UserResource findUser(UUID uuid) {
        final var user = userRepository.findByUUIDOrElseThrow(uuid);
        return UserResource.from(user);
    }
}
