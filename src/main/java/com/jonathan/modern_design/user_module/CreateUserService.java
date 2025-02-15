package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design._infra.config.annotations.DomainService;
import com.jonathan.modern_design.user_module.dtos.CreateUserCommand;
import com.jonathan.modern_design.user_module.model.UserEmail;
import com.jonathan.modern_design.user_module.model.UserName;
import com.jonathan.modern_design.user_module.model.UserPassword;
import com.jonathan.modern_design.user_module.model.UserRealName;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
class CreateUserService implements CreateUserUseCase {
    private final UserRepository repository;

    @Override
    public User createUser(CreateUserCommand command) {
        final var user = User.builder()
                .uuid(command.uuid())
                .realname(UserRealName.of(command.realname()))
                .username(UserName.of(command.username()))
                .email(UserEmail.of(command.email()))
                .password(UserPassword.of(command.password()))
                .country(command.country())
                .build();

        return repository.createUser(user);
    }
}
