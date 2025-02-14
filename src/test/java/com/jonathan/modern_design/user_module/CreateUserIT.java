package com.jonathan.modern_design.user_module;


import com.jonathan.modern_design.__config.RepositoryITConfig;
import org.junit.jupiter.api.Test;

import static com.jonathan.modern_design._fake_data.CreateUserMother.createUserCommandWithValidData;
import static org.assertj.core.api.Assertions.assertThat;

class CreateUserIT extends RepositoryITConfig {
    private final UserRepository repository = new InMemoryUserRepository();
    private final UserFacade userFacade = new UserConfiguration().userFacade(repository);

    @Test
    void should_create_user() {
        assertThat(userFacade.createUser(createUserCommandWithValidData())).isNotNull();
    }
}
