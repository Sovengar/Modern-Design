package com.jonathan.modern_design.user_module;


import com.jonathan.modern_design.config.RepositoryIntegrationTestConfig;
import com.jonathan.modern_design.fake_data.CreateUserMother;
import org.junit.jupiter.api.Test;

import static com.jonathan.modern_design.fake_data.CreateUserMother.normalUser;
import static org.assertj.core.api.Assertions.assertThat;

class UserIT extends RepositoryIntegrationTestConfig {
    private final UserConfigurationFactory factory = new UserConfigurationFactory();
    private final UserRepository repository = new UserRepositoryFake();

    @Test
    void should_create_user() {
        UserFacade userFacade = factory.userFacade(repository);
        assertThat(userFacade.createUser(normalUser())).isNotNull();
    }
}
