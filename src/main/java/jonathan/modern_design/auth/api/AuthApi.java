package jonathan.modern_design.auth.api;

import jonathan.modern_design._shared.tags.Facade;
import jonathan.modern_design.auth.api.dtos.UserDto;
import jonathan.modern_design.auth.application.RegisterUser;
import jonathan.modern_design.auth.domain.models.User;
import jonathan.modern_design.auth.queries.FindUser;
import lombok.RequiredArgsConstructor;

//Here we have the behavior we want to expose to other modules, my UI can call more methods because is on the same logical boundary
public interface AuthApi {
    void registerUser(RegisterUser.Command command);

    UserDto findUser(User.Id userId);

    @Facade
    @RequiredArgsConstructor
    class AuthInternalApi implements AuthApi {
        private final RegisterUser registerUser;
        private final FindUser findUser;

        @Override
        public void registerUser(RegisterUser.Command command) {
            registerUser.handle(command);
        }

        @Override
        public UserDto findUser(User.Id userId) {
            return findUser.queryWith(userId);
        }
    }
}
