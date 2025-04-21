package jonathan.modern_design.user.api;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design.user.api.dtos.UserDto;
import jonathan.modern_design.user.application.FindUser;
import jonathan.modern_design.user.application.RegisterUser;
import jonathan.modern_design.user.domain.models.User;
import lombok.RequiredArgsConstructor;

public interface UserApi {
    void registerUser(RegisterUser.Command command);

    UserDto findUser(User.UserId userId);

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
        public UserDto findUser(User.UserId userId) {
            return findUser.queryWith(userId);
        }
    }
}
