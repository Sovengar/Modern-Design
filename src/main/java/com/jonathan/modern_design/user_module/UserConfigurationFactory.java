package com.jonathan.modern_design.user_module;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfigurationFactory {

    @Bean
    private CreateUserUseCase createUserUseCase(UserRepository userRepository) {
        return new CreateUserService(userRepository);
    }

    @Bean(name = "userFacade")
    public UserFacade userFacade(UserRepository userRepository) {
        CreateUserUseCase createUserUseCase = createUserUseCase(userRepository);
        return new UserFacade(userRepository, createUserUseCase);
    }
}
