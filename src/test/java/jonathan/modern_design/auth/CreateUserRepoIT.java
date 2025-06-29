package jonathan.modern_design.auth;

import jonathan.modern_design.__config.shared_for_all_tests_in_class.ITConfig;
import jonathan.modern_design.auth.api.AuthApi;
import jonathan.modern_design.auth.domain.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static jonathan.modern_design.__config.dsl.UserStub.CreateValidUser.createUserCommandWithValidData;
import static org.assertj.core.api.Assertions.assertThat;

class CreateUserRepoIT extends ITConfig {

    @Autowired
    private AuthApi authApi;

    @Test
    void should_register_user() {
        var data = createUserCommandWithValidData();
        authApi.registerUser(data);
        var user = authApi.findUser(User.Id.of(data.id()));
        assertThat(user).isNotNull();
    }
}
