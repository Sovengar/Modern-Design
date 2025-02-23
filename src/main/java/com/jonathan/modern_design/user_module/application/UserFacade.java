package com.jonathan.modern_design.user_module.application;

import com.jonathan.modern_design._infra.config.annotations.BeanClass;
import com.jonathan.modern_design.user_module.domain.UserRepository;
import com.jonathan.modern_design.user_module.domain.model.User;
import com.jonathan.modern_design.user_module.domain.services.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@BeanClass
@RequiredArgsConstructor
@Transactional
public class UserFacade implements RegisterUserUseCase {
    private final UserRepository userRepository;
    private final RegisterUserUseCase registerUserUseCase;

    @Override
    public User registerUser(RegisterUserCommand command) {
        return registerUserUseCase.registerUser(command);
    }
}
