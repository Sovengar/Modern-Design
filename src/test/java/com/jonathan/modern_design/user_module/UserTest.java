package com.jonathan.modern_design.user_module;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    private final UserConfigurationFactory factory = new UserConfigurationFactory();
    private final UserRepository repository = new InMemoryUserRepository();

    @Test
    void should_create_user() {
        UserFacade userFacade = factory.userFacade(repository);

        assertThat(userFacade.createUser(User.create("a","a","a","a","a","a"))).isNotNull();
    }
}
