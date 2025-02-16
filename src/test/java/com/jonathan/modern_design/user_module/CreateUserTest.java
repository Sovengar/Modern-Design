package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.__config.PrettyTestNames;
import com.jonathan.modern_design.user_module.application.UserFacade;
import com.jonathan.modern_design.user_module.domain.UserRepository;
import com.jonathan.modern_design.user_module.infra.InMemoryUserRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;

import static com.jonathan.modern_design._fake_data.CreateUserMother.createUserCommandWithValidData;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(PrettyTestNames.class)
class CreateUserTest {
    private final UserConfiguration factory = new UserConfiguration();
    private final UserRepository repository = new InMemoryUserRepository();

    @Test
    void should_create_user() {
        UserFacade userFacade = factory.userFacade(repository);
        assertThat(userFacade.registerUser(createUserCommandWithValidData())).isNotNull();
    }

    //TODO VALIDACIONES MIN PASSWORD, MAX, EMAIL... UUID?
}
