package com.jonathan.modern_design.user_module.user.application;

import com.jonathan.modern_design._infra.config.annotations.DomainService;
import com.jonathan.modern_design.user_module.role.Role;
import com.jonathan.modern_design.user_module.role.RoleRepo;
import com.jonathan.modern_design.user_module.role.Roles;
import com.jonathan.modern_design.user_module.user.domain.UserRepo;
import com.jonathan.modern_design.user_module.user.domain.model.User;
import com.jonathan.modern_design.user_module.user.domain.model.UserEmail;
import com.jonathan.modern_design.user_module.user.domain.model.UserName;
import com.jonathan.modern_design.user_module.user.domain.model.UserPassword;
import com.jonathan.modern_design.user_module.user.domain.model.UserRealName;
import com.jonathan.modern_design.user_module.user.dtos.UserRegisterCommand;
import lombok.RequiredArgsConstructor;

import static java.lang.String.format;

@DomainService
@RequiredArgsConstructor
public class UserRegister {
    private final UserRepo repository;
    private final RoleRepo roleRepo;

    public void registerUser(UserRegisterCommand command) {
        repository.findByUuid(new User.UserId(command.uuid())).ifPresent(user -> {
            throw new UserAlreadyExistsException(format("User [%s] already exists", command.uuid()));
        });

        //Begin of Complex logic to know the final role of the user
        var roleCode = new Role.Code(Roles.USER.getCode());
        var role = roleRepo.findByCode(roleCode);
        //End of complex logic

        //Complex logic to decide the user
        var user = User.register(new User.UserId(command.uuid()), UserRealName.of(command.realname().orElse("")), UserName.of(command.username()), UserEmail.of(command.email()), UserPassword.of(command.password()), command.country(), role);
        repository.registerUser(user);
    }

    private static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }
}
