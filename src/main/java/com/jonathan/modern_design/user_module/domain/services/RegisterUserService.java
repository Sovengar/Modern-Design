package com.jonathan.modern_design.user_module.domain.services;

import com.jonathan.modern_design._infra.config.annotations.DomainService;
import com.jonathan.modern_design.user_module.application.RegisterUserUseCase;
import com.jonathan.modern_design.user_module.domain.UserRepository;
import com.jonathan.modern_design.user_module.domain.model.Role;
import com.jonathan.modern_design.user_module.domain.model.Roles;
import com.jonathan.modern_design.user_module.domain.model.User;
import com.jonathan.modern_design.user_module.infra.role.RoleRepo;
import lombok.RequiredArgsConstructor;

import static java.lang.String.format;

@DomainService
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {
    private final UserRepository repository;
    private final RoleRepo roleRepo;

    @Override
    public User.ID registerUser(RegisterUserCommand command) {
        repository.findByUuid(new User.ID(command.uuid())).ifPresent(user -> {
            throw new UserAlreadyExistsException(format("User [%s] already exists", command.uuid()));
        });

        //Begin of Complex logic to know the final role of the user
        var role = roleRepo.findByCode(new Role.Code(Roles.USER.getCode()));
        //End of complex logic

        //Complex logic to decide the user
        var user = User.register(new User.ID(command.uuid()), command.realname().orElse(""), command.username(), command.email(), command.password(), command.country(), role);
        repository.createUser(user);
        return user.getUuid();
    }

    private static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }
}
