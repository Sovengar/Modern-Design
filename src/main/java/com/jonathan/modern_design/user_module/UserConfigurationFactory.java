package com.jonathan.modern_design.user_module;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfigurationFactory {

    @Bean(name = "userRepositorySpringAdapter")
    public UserRepository userRepository(SpringUserRepository springUserRepository) {
        return new UserRepositorySpringAdapter(springUserRepository);
    }

    @Bean(name = "userFacade")
    public UserFacade userFacade(UserRepository userRepository) {
        CreateUserUseCase createUserUseCase = new CreateUserService(userRepository);
        return new UserFacade(userRepository, createUserUseCase);
    }
}
