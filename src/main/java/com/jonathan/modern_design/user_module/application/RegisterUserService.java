package com.jonathan.modern_design.user_module.application;

import com.jonathan.modern_design._infra.config.annotations.DomainService;
import com.jonathan.modern_design.user_module.domain.UserRepository;
import com.jonathan.modern_design.user_module.domain.model.User;
import com.jonathan.modern_design.user_module.domain.model.UserEmail;
import com.jonathan.modern_design.user_module.domain.model.UserName;
import com.jonathan.modern_design.user_module.domain.model.UserPassword;
import com.jonathan.modern_design.user_module.domain.model.UserRealName;
import com.jonathan.modern_design.user_module.domain.services.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {
    private final UserRepository repository;

    @Override
    public User registerUser(RegisterUserCommand command) {
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
