package jonathan.modern_design.auth.infra;

import jonathan.modern_design._shared.other.country.CountriesInventory;
import jonathan.modern_design._shared.other.country.CountriesInventoryStub;
import jonathan.modern_design.auth.api.UserApi;
import jonathan.modern_design.auth.application.RegisterUser;
import jonathan.modern_design.auth.application.queries.FindUser;
import jonathan.modern_design.auth.domain.store.RoleStore;
import jonathan.modern_design.auth.domain.store.RoleStoreInMemory;
import jonathan.modern_design.auth.domain.store.UserInMemoryRepo;
import jonathan.modern_design.auth.domain.store.UserRepo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class UsersConfig {
    final UserRepo userRepo = new UserInMemoryRepo();

    public UserApi userApi(UserRepo userRepo, RoleStore roleStore, CountriesInventory countriesInventory) {
        var registerUser = new RegisterUser(userRepo, roleStore, countriesInventory);
        var userFinder = new FindUser(userRepo);
        return new UserApi.UserInternalApi(registerUser, userFinder);
    }

    @Profile("test")
    public UserApi userApi() {
        //For Unit testing
        var roleRepo = new RoleStoreInMemory();
        final CountriesInventory countriesInventory = new CountriesInventoryStub();

        return userApi(userRepo, roleRepo, countriesInventory);
    }

    @Profile("test")
    public UserRepo getUserRepo() {
        //For Unit testing
        return userRepo;
    }
}
