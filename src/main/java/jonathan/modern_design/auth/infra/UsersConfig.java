package jonathan.modern_design.auth.infra;

import jonathan.modern_design.auth.api.AuthApi;
import jonathan.modern_design.auth.application.RegisterUser;
import jonathan.modern_design.auth.domain.store.RoleStore;
import jonathan.modern_design.auth.domain.store.UserRepo;
import jonathan.modern_design.auth.infra.store.repositories.inmemory.RoleInMemoryRepo;
import jonathan.modern_design.auth.infra.store.repositories.inmemory.UserInMemoryRepo;
import jonathan.modern_design.auth.queries.FindUser;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class UsersConfig {
    final UserRepo userRepo = new UserInMemoryRepo();

    public AuthApi userApi(UserRepo userRepo, RoleStore roleStore) {
        var registerUser = new RegisterUser(userRepo, roleStore);
        var userFinder = new FindUser(userRepo);
        return new AuthApi.AuthInternalApi(registerUser, userFinder);
    }

    @Profile("test")
    public AuthApi userApi() {
        //For Unit testing
        var roleRepo = new RoleInMemoryRepo();

        return userApi(userRepo, roleRepo);
    }

    @Profile("test")
    public UserRepo getUserRepo() {
        //For Unit testing
        return userRepo;
    }
}
