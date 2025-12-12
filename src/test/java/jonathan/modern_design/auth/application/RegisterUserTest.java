package jonathan.modern_design.auth.application;

import jonathan.modern_design.auth.domain.catalogs.Roles;
import jonathan.modern_design.auth.domain.models.Role;
import jonathan.modern_design.auth.domain.models.User;
import jonathan.modern_design.auth.domain.vo.UserName;
import jonathan.modern_design.auth.domain.vo.UserPassword;
import jonathan.modern_design.auth.infra.store.repositories.inmemory.RoleInMemoryRepo;
import jonathan.modern_design.auth.infra.store.repositories.inmemory.UserInMemoryRepo;
import jonathan.modern_design._shared.domain.vo.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegisterUserTest {

    private UserInMemoryRepo repository;
    private RoleInMemoryRepo roleStore;
    private RegisterUser registerUser;

    @BeforeEach
    void setUp() {
        repository = new UserInMemoryRepo();
        roleStore = new RoleInMemoryRepo();
        registerUser = new RegisterUser(repository, roleStore);
    }

    @Test
    void should_create_user_successfully_when_not_exists() {
        var command = new RegisterUser.Command(UUID.randomUUID(), "username", "email@test.com", "Password123!");

        var result = registerUser.handle(command);

        assertEquals(command.id(), result);

        var savedUser = repository.findById(User.Id.of(command.id()));
        assertTrue(savedUser.isPresent());
        assertEquals("username", savedUser.get().getUsername().getUsername());
        assertEquals("EMAIL@TEST.COM", savedUser.get().getEmail().getEmail());
    }

    @Test
    void should_return_existing_id_when_user_already_exists_idempotency() {
        var command = new RegisterUser.Command(UUID.randomUUID(), "username", "email@test.com", "Password123!");

        // Pre-create user in repository
        var role = roleStore.findByCode(Role.Code.of(Roles.USER.getCode()));
        var existingUser = User.Factory.register(User.Id.of(command.id()), UserName.of("username"),
                Email.of("email@test.com"), UserPassword.of("Password123!"), role);
        repository.registerUser(existingUser);

        var result = registerUser.handle(command);

        assertEquals(command.id(), result);
        // Ensure it's still the same user
        var savedUser = repository.findById(User.Id.of(command.id()));
        assertTrue(savedUser.isPresent());
    }
}
