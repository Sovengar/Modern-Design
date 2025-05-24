package jonathan.modern_design.user.infra;

import jonathan.modern_design._shared.country.CountriesInventory;
import jonathan.modern_design._shared.country.CountriesInventoryStub;
import jonathan.modern_design.user.api.UserApi;
import jonathan.modern_design.user.application.RegisterUser;
import jonathan.modern_design.user.application.queries.FindUser;
import jonathan.modern_design.user.domain.store.RoleStore;
import jonathan.modern_design.user.domain.store.RoleStoreInMemory;
import jonathan.modern_design.user.domain.store.UserInMemoryRepo;
import jonathan.modern_design.user.domain.store.UserRepo;
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
