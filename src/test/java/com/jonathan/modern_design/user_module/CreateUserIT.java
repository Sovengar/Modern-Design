package com.jonathan.modern_design.user_module;


import com.jonathan.modern_design.config.PrettyTestNames;
import com.jonathan.modern_design.config.RepositoryIntegrationTestConfig;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;

import static com.jonathan.modern_design.fake_data.CreateUserMother.createUserCommandWithValidData;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(PrettyTestNames.class)
class CreateUserIT extends RepositoryIntegrationTestConfig {
    private final UserConfiguration factory = new UserConfiguration();
    private final UserRepository repository = new InMemoryUserRepository();

    @Test
    void should_create_user() {
        UserFacade userFacade = factory.userFacade(repository);
        assertThat(userFacade.createUser(createUserCommandWithValidData())).isNotNull();
    }
}
