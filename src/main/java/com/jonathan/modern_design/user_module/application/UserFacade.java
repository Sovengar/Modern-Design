package com.jonathan.modern_design.user_module.application;

import com.jonathan.modern_design._internal.config.annotations.Inyectable;
import com.jonathan.modern_design.user_module.UserApi;
import com.jonathan.modern_design.user_module.domain.User.UserId;
import com.jonathan.modern_design.user_module.domain.UserRepo;
import com.jonathan.modern_design.user_module.dtos.UserRegisterCommand;
import com.jonathan.modern_design.user_module.dtos.UserResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Inyectable
@RequiredArgsConstructor
@Slf4j
public class UserFacade implements UserApi {
    private final UserRepo userRepo;
    private final UserRegister userRegister;

    @Override
    @Transactional
    public void registerUser(UserRegisterCommand command) {
        log.info("BEGIN RegisterUser");
        userRegister.registerUser(command);
        log.info("END RegisterUser");
    }

    @Override
    public UserResource findUser(UserId userId) {
        log.info("BEGIN FindUser");
        final var user = userRepo.findByUUIDOrElseThrow(userId);
        var userResource = UserResource.from(user);

        log.info("END FindUser");
        return userResource;
    }
}
