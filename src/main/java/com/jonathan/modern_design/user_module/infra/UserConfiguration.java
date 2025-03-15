package com.jonathan.modern_design.user_module.infra;

import com.jonathan.modern_design.user_module.UserApi;
import com.jonathan.modern_design.user_module.application.UserFacade;
import com.jonathan.modern_design.user_module.application.UserRegister;
import com.jonathan.modern_design.user_module.domain.RoleRepo;
import com.jonathan.modern_design.user_module.domain.UserRepo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class UserConfiguration {
    public UserApi userApi(UserRepo userRepo, RoleRepo roleRepo) {
        UserRegister userRegister = new UserRegister(userRepo, roleRepo);
        return new UserFacade(userRepo, userRegister);
    }

    @Profile("test")
    public UserApi userApi() {
        UserRepo userRepo = new UserInMemoryRepo();
        RoleRepo roleRepo = new RoleRepoInMemory();
        return userApi(userRepo, roleRepo);
    }
}
