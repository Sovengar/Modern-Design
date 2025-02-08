package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.user_module.dtos.CreateUserCommand;
import com.jonathan.modern_design.user_module.vo.UserEmailVO;
import com.jonathan.modern_design.user_module.vo.UserNameVO;
import com.jonathan.modern_design.user_module.vo.UserPasswordVO;
import com.jonathan.modern_design.user_module.vo.UserRealNameVO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CreateUserService implements CreateUserUseCase {
    private final UserRepository repository;

    @Override
    public User createUser(CreateUserCommand command) {
        final var user = User.builder()
                .realname(UserRealNameVO.of(command.realname()))
                .username(UserNameVO.of(command.username()))
                .email(UserEmailVO.of(command.email()))
                .password(UserPasswordVO.of(command.password()))
                .country(command.country())
                .build();

        return repository.createUser(user);
    }
}
