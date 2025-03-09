package com.jonathan.modern_design.user_module;


import com.jonathan.modern_design.__config.RepositoryITConfig;
import com.jonathan.modern_design.user_module.user.infra.UserRepoAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static com.jonathan.modern_design._fake_data.UserStub.CreateValidUser.createUserCommandWithValidData;
import static org.assertj.core.api.Assertions.assertThat;

@Import(UserConfiguration.class)
class CreateUserRepoIT extends RepositoryITConfig {

    @Autowired
    private UserRepoAdapter repository;

    @Autowired
    private UserFacade userFacade;

    @Test
    void should_register_user() {
        assertThat(userFacade.registerUser(createUserCommandWithValidData())).isNotNull();
    }
}
