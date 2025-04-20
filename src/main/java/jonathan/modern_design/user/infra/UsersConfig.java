package jonathan.modern_design.user.infra;

import jonathan.modern_design.user.UserApi;
import jonathan.modern_design.user.application.FindUser;
import jonathan.modern_design.user.application.RegisterUser;
import jonathan.modern_design.user.domain.repos.RoleInMemoryRepo;
import jonathan.modern_design.user.domain.repos.RoleRepo;
import jonathan.modern_design.user.domain.repos.UserInMemoryRepo;
import jonathan.modern_design.user.domain.repos.UserRepo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class UsersConfig {
    final UserRepo userRepo = new UserInMemoryRepo();

    public UserApi userApi(UserRepo userRepo, RoleRepo roleRepo) {
        var registerUser = new RegisterUser(userRepo, roleRepo);
        var userFinder = new FindUser(userRepo);
        return new UserApi.UserInternalApi(registerUser, userFinder);
    }

    @Profile("test")
    public UserApi userApi() {
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
