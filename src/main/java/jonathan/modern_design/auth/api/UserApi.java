package jonathan.modern_design.auth.api;

import jonathan.modern_design._shared.tags.Injectable;
import jonathan.modern_design.auth.api.dtos.UserDto;
import jonathan.modern_design.auth.application.RegisterUser;
import jonathan.modern_design.auth.application.queries.FindUser;
import jonathan.modern_design.auth.domain.models.User;
import lombok.RequiredArgsConstructor;

public interface UserApi {
    void registerUser(RegisterUser.Command command);

    UserDto findUser(User.Id userId);

    @Injectable
    @RequiredArgsConstructor
    class UserInternalApi implements UserApi {
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
