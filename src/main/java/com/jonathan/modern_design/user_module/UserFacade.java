package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.common.BeanClass;
import com.jonathan.modern_design.user_module.dtos.CreateUserCommand;
import lombok.RequiredArgsConstructor;

@BeanClass
@RequiredArgsConstructor
public class UserFacade implements CreateUserUseCase {
    private final UserRepository userRepository;
    private final CreateUserUseCase createUserUseCase;

    @Override
    public User createUser(CreateUserCommand command) {
        return createUserUseCase.createUser(command);
    }
}
