package jonathan.modern_design.auth.application;

import jonathan.modern_design.__config.shared_for_all_classes.AcceptanceTest;
import jonathan.modern_design.__config.shared_for_all_classes.EnableTestContainers;
import jonathan.modern_design.auth.api.AuthApi;
import jonathan.modern_design.auth.domain.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;

import static jonathan.modern_design.auth.application.CreateUserDsl.withValidData;
import static org.assertj.core.api.Assertions.assertThat;

@ApplicationModuleTest //Better than @SpringBootTest when using modules
@AcceptanceTest
@EnableTestContainers
class CreateUserIT {

    @Autowired
    private AuthApi authApi;

    @Test
    void should_register_user() {
        var data = withValidData();
        authApi.registerUser(data);
        var user = authApi.findUser(User.Id.of(data.id()));
        assertThat(user).isNotNull();
    }
}
