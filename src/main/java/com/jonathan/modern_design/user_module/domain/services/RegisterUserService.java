package com.jonathan.modern_design.user_module.domain.services;

import com.jonathan.modern_design._infra.config.annotations.DomainService;
import com.jonathan.modern_design.user_module.application.RegisterUserUseCase;
import com.jonathan.modern_design.user_module.domain.UserRepository;
import com.jonathan.modern_design.user_module.domain.model.User;
import com.jonathan.modern_design.user_module.domain.model.UserEmail;
import com.jonathan.modern_design.user_module.domain.model.UserName;
import com.jonathan.modern_design.user_module.domain.model.UserPassword;
import com.jonathan.modern_design.user_module.domain.model.UserRealName;
import lombok.RequiredArgsConstructor;

import static java.lang.String.format;

@DomainService
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {
    private final UserRepository repository;

    @Override
    public User registerUser(RegisterUserCommand command) {

        repository.findById(command.uuid()).ifPresent(user -> {
            throw new UserAlreadyExistsException(format("User [%s] already exists", command.uuid()));
        });

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

    private static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }
}
