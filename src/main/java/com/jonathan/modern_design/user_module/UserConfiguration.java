package com.jonathan.modern_design.user_module;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserConfiguration {

    @Bean
    public UserMapper userMapper() {
        return new UserMapperAdapter();
    }

    @Bean
    public UserRepository userRepository(SpringUserRepository repository, UserMapper userMapper) {
        return new UserRepositorySpringAdapter(repository, userMapper);
    }

    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository userRepository) {
        return new CreateUserService(userRepository);
    }

    @Bean
    public UserFacade userFacade(UserRepository userRepository) {
        CreateUserUseCase createUserUseCase = createUserUseCase(userRepository);
        return new UserFacade(userRepository, createUserUseCase);
    }
}
