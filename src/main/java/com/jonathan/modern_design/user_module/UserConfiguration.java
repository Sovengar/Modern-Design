package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.user_module.role.RoleRepo;
import com.jonathan.modern_design.user_module.role.RoleRepoInMemory;
import com.jonathan.modern_design.user_module.user.application.UserRegister;
import com.jonathan.modern_design.user_module.user.domain.UserRepo;
import com.jonathan.modern_design.user_module.user.infra.UserInMemoryRepo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
class UserConfiguration {
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
