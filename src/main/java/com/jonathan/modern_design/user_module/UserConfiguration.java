package com.jonathan.modern_design.user_module;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserConfiguration {

    @Bean
    public UserRepository userRepository(SpringUserRepository repository) {
        return new UserRepositorySpringAdapter(repository, new UserMapperAdapter());
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

//@ComponentScan(
//        basePackageClasses = {UserFacade.class},
//        includeFilters = {
//                @Filter(type = FilterType.ANNOTATION, classes = {DomainService.class, Stub.class, Fake.class})
//        },
//        excludeFilters = {
//                //@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {someExternalAPIStub.class})
//        }
//)
