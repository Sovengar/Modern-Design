package com.jonathan.modern_design.user_module.user.domain.services;

import com.jonathan.modern_design._infra.config.annotations.DomainService;
import com.jonathan.modern_design.user_module.role.RoleRepo;
import com.jonathan.modern_design.user_module.role.Roles;
import com.jonathan.modern_design.user_module.user.application.RegisterUserUseCase;
import com.jonathan.modern_design.user_module.user.domain.UserRepo;
import com.jonathan.modern_design.user_module.user.domain.model.User;
import lombok.RequiredArgsConstructor;

import static java.lang.String.format;

@DomainService
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {
    private final UserRepo repository;
    private final RoleRepo roleRepo;

    @Override
    public User.ID registerUser(RegisterUserCommand command) {
        repository.findByUuid(new User.ID(command.uuid())).ifPresent(user -> {
            throw new UserAlreadyExistsException(format("User [%s] already exists", command.uuid()));
        });

        //Begin of Complex logic to know the final role of the user
        var role = roleRepo.findByCode(Roles.USER.getDescription()); //TODO VER SI PUEDES HACERLO FUNCIONAR CON EL MICROTYPE
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
