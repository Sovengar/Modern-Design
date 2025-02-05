package com.jonathan.modern_design.user_module;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserConfigurationFactory {

    @Bean
    public UserRepository userRepository(SpringUserRepository repository) {
        return new UserRepositorySpringAdapter(repository);
    }

    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository userRepository) {
        return new CreateUserService(userRepository);
    }

    @Bean(name = "userFacade")
    public UserFacade userFacade(UserRepository userRepository) {
        CreateUserUseCase createUserUseCase = createUserUseCase(userRepository);
        return new UserFacade(userRepository, createUserUseCase);
    }
}
