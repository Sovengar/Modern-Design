package jonathan.modern_design.user.application;

import jonathan.modern_design._common.annotations.Inyectable;
import jonathan.modern_design.user.UserApi;
import jonathan.modern_design.user.domain.User.UserId;
import jonathan.modern_design.user.domain.UserRepo;
import jonathan.modern_design.user.dtos.UserDto;
import jonathan.modern_design.user.dtos.UserRegisterCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Inyectable
@RequiredArgsConstructor
@Slf4j
public class UserFacade implements UserApi {
    private final UserRepo userRepo;
    private final UserRegister userRegister;

    @Override
    @Transactional
    public void registerUser(UserRegisterCommand command) {
        log.info("BEGIN RegisterUser");
        userRegister.registerUser(command);
        log.info("END RegisterUser");
    }

    @Override
    public UserDto findUser(UserId userId) {
        log.info("BEGIN FindUser");
        final var user = userRepo.findByUUIDOrElseThrow(userId);

        //TODO var userResource = UserResource.from(user);
        var userResource = new UserDto("a", "a", "a", "a", null);

        log.info("END FindUser");
        return userResource;
    }
}
