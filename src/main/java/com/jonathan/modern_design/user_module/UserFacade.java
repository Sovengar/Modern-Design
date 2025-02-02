package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.common.BeanClass;
import lombok.RequiredArgsConstructor;

@BeanClass
@RequiredArgsConstructor
public class UserFacade implements CreateUserUseCase {
    private final UserRepository userRepository;
    private final CreateUserUseCase createUserUseCase;

    @Override
    public User createUser(User user) {
        return createUserUseCase.createUser(user);
    }
}
