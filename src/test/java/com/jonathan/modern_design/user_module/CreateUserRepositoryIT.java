package com.jonathan.modern_design.user_module;


import com.jonathan.modern_design.__config.RepositoryITConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static com.jonathan.modern_design._fake_data.CreateUserMother.createUserCommandWithValidData;
import static org.assertj.core.api.Assertions.assertThat;

@Import(UserConfiguration.class)
class CreateUserIT extends RepositoryITConfig {

    @Autowired
    private UserRepositorySpringAdapter repository;

    @Autowired
    private UserFacade userFacade;

    @Test
    void should_create_user() {
        assertThat(userFacade.createUser(createUserCommandWithValidData())).isNotNull();
    }
}
