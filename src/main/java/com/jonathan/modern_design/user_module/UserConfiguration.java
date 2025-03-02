package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.user_module.application.RegisterUserUseCase;
import com.jonathan.modern_design.user_module.domain.UserRepository;
import com.jonathan.modern_design.user_module.domain.services.RegisterUserService;
import com.jonathan.modern_design.user_module.infra.UserMapper;
import com.jonathan.modern_design.user_module.infra.UserMapperAdapter;
import com.jonathan.modern_design.user_module.infra.UserPersistenceAdapter;
import com.jonathan.modern_design.user_module.infra.UserSpringRepo;
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
    public RegisterUserUseCase createUserUseCase(UserRepository userRepository) {
        return new RegisterUserService(userRepository);
    }

    @Bean
    public UserFacade userFacade(UserRepository userRepository) {
        RegisterUserUseCase registerUserUseCase = createUserUseCase(userRepository);
        return new UserFacade(userRepository, registerUserUseCase);
    }
}
