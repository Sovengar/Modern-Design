package com.jonathan.modern_design.user_module.domain.services;

import com.jonathan.modern_design._infra.config.annotations.DomainService;
import com.jonathan.modern_design.user_module.application.RegisterUserUseCase;
import com.jonathan.modern_design.user_module.domain.UserRepository;
import com.jonathan.modern_design.user_module.domain.model.User;
import com.jonathan.modern_design.user_module.domain.model.UserIdentifiers;
import lombok.RequiredArgsConstructor;

import static java.lang.String.format;

@DomainService
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {
    private final UserRepository repository;

    @Override
    public UserIdentifiers registerUser(RegisterUserCommand command) {
        repository.findByUuid(command.uuid()).ifPresent(user -> {
            throw new UserAlreadyExistsException(format("User [%s] already exists", command.uuid()));
        });

        var user = User.register(command.uuid(), command.realname().orElse(""), command.username(), command.email(), command.password(), command.country());
        user = repository.createUser(user);
        return new UserIdentifiers(user.getUserId(), user.getUuid());
    }

    private static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }
}
