package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.user_module.role.RoleRepo;
import com.jonathan.modern_design.user_module.role.RoleRepoAdapter;
import com.jonathan.modern_design.user_module.role.RoleRepoInMemory;
import com.jonathan.modern_design.user_module.role.RoleSpringRepo;
import com.jonathan.modern_design.user_module.user.application.RegisterUserUseCase;
import com.jonathan.modern_design.user_module.user.domain.UserRepo;
import com.jonathan.modern_design.user_module.user.domain.services.RegisterUserService;
import com.jonathan.modern_design.user_module.user.infra.UserInMemoryRepo;
import com.jonathan.modern_design.user_module.user.infra.UserMapper;
import com.jonathan.modern_design.user_module.user.infra.UserMapperAdapter;
import com.jonathan.modern_design.user_module.user.infra.UserRepoAdapter;
import com.jonathan.modern_design.user_module.user.infra.UserSpringRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserConfiguration {

    @Bean
    public UserMapper userMapper() {
        return new UserMapperAdapter();
    }

    @Bean
    public UserRepo userRepository(UserSpringRepo repository) {
        return new UserRepoAdapter(repository);
    }

    @Bean
    public RegisterUserUseCase createUserUseCase(UserRepo userRepo, RoleRepo roleRepo) {
        return new RegisterUserService(userRepo, roleRepo);
    }

    @Bean
    public UserFacade userFacade(UserRepo userRepo, RoleRepo roleRepo) {
        RegisterUserUseCase registerUserUseCase = createUserUseCase(userRepo, roleRepo);
        return new UserFacade(userRepo, registerUserUseCase);
    }

    @Bean
    public RoleRepo roleRepo(RoleSpringRepo roleRepo) {
        return new RoleRepoAdapter(roleRepo);
    }

    public UserFacade userFacade() {
        UserRepo userRepo = new UserInMemoryRepo();
        RoleRepo roleRepo = new RoleRepoInMemory();
        return userFacade(userRepo, roleRepo);
    }
}
