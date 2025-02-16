package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.user_module.application.CreateUserService;
import com.jonathan.modern_design.user_module.application.CreateUserUseCase;
import com.jonathan.modern_design.user_module.application.UserFacade;
import com.jonathan.modern_design.user_module.domain.UserRepository;
import com.jonathan.modern_design.user_module.infra.SpringUserRepository;
import com.jonathan.modern_design.user_module.infra.UserMapper;
import com.jonathan.modern_design.user_module.infra.UserMapperAdapter;
import com.jonathan.modern_design.user_module.infra.UserRepositorySpringAdapter;
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
