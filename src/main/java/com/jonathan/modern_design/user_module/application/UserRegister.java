package com.jonathan.modern_design.user_module.application;

import com.jonathan.modern_design._infra.config.annotations.Inyectable;
import com.jonathan.modern_design.user_module.domain.Role;
import com.jonathan.modern_design.user_module.domain.RoleRepo;
import com.jonathan.modern_design.user_module.domain.Roles;
import com.jonathan.modern_design.user_module.domain.User;
import com.jonathan.modern_design.user_module.domain.User.UserId;
import com.jonathan.modern_design.user_module.domain.UserRepo;
import com.jonathan.modern_design.user_module.domain.vo.UserEmail;
import com.jonathan.modern_design.user_module.domain.vo.UserName;
import com.jonathan.modern_design.user_module.domain.vo.UserPassword;
import com.jonathan.modern_design.user_module.domain.vo.UserRealName;
import com.jonathan.modern_design.user_module.dtos.UserRegisterCommand;
import lombok.RequiredArgsConstructor;

import java.io.Serial;

import static java.lang.String.format;

@Inyectable
@RequiredArgsConstructor
public class UserRegister {
    private final UserRepo repository;
    private final RoleRepo roleRepo;

    public void registerUser(UserRegisterCommand command) {
        repository.findByUuid(new UserId(command.uuid())).ifPresent(user -> {
            throw new UserAlreadyExistsException(format("User [%s] already exists", command.uuid()));
        });

        //Begin of Complex logic to know the final role of the user
        var roleCode = new Role.Code(Roles.USER.getCode());
        var role = roleRepo.findByCode(roleCode);
        //End of complex logic

        //Complex logic to decide the user
        var user = User.register(new UserId(command.uuid()), UserRealName.of(command.realname().orElse("")), UserName.of(command.username()), UserEmail.of(command.email()), UserPassword.of(command.password()), command.country(), role);
        repository.registerUser(user);
    }

    private static class UserAlreadyExistsException extends RuntimeException {
        @Serial private static final long serialVersionUID = 1604523616703390261L;

        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }
}
