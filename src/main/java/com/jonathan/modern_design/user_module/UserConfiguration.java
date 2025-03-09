package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.user_module.application.RegisterUserUseCase;
import com.jonathan.modern_design.user_module.domain.UserRepository;
import com.jonathan.modern_design.user_module.domain.services.RegisterUserService;
import com.jonathan.modern_design.user_module.infra.UserInMemoryRepo;
import com.jonathan.modern_design.user_module.infra.UserMapper;
import com.jonathan.modern_design.user_module.infra.UserMapperAdapter;
import com.jonathan.modern_design.user_module.infra.UserPersistenceAdapter;
import com.jonathan.modern_design.user_module.infra.UserSpringRepo;
import com.jonathan.modern_design.user_module.infra.role.RoleRepo;
import com.jonathan.modern_design.user_module.infra.role.RoleRepoAdapter;
import com.jonathan.modern_design.user_module.infra.role.RoleRepoInMemory;
import com.jonathan.modern_design.user_module.infra.role.RoleSpringRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserConfiguration {

    @Bean
    public UserMapper userMapper() {
        return new UserMapperAdapter();
    }

    @Bean
    public UserRepository userRepository(UserSpringRepo repository) {
        return new UserPersistenceAdapter(repository);
    }

    @Bean
    public RegisterUserUseCase createUserUseCase(UserRepository userRepository, RoleRepo roleRepo) {
        return new RegisterUserService(userRepository, roleRepo);
    }

    @Bean
    public UserFacade userFacade(UserRepository userRepository, RoleRepo roleRepo) {
        RegisterUserUseCase registerUserUseCase = createUserUseCase(userRepository, roleRepo);
        return new UserFacade(userRepository, registerUserUseCase);
    }

    @Bean
    public RoleRepo roleRepo(RoleSpringRepo roleRepo) {
        return new RoleRepoAdapter(roleRepo);
    }

    public UserFacade userFacade() {
        UserRepository userRepository = new UserInMemoryRepo();
        RoleRepo roleRepo = new RoleRepoInMemory();
        return userFacade(userRepository, roleRepo);
    }
}
