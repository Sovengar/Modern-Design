package com.jonathan.modern_design.user_module.application;

import com.jonathan.modern_design._infra.config.annotations.BeanClass;
import com.jonathan.modern_design.user_module.domain.User;
import com.jonathan.modern_design.user_module.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@BeanClass
@RequiredArgsConstructor
@Transactional
public class UserFacade implements CreateUserUseCase {
    private final UserRepository userRepository;
    private final CreateUserUseCase createUserUseCase;

    @Override
    public User createUser(CreateUserCommand command) {
        return createUserUseCase.createUser(command);
    }
}
