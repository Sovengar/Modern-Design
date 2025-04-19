package jonathan.modern_design.user.application;

import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design.user.UserApi;
import jonathan.modern_design.user.domain.User;
import jonathan.modern_design.user.domain.repos.UserRepo;
import jonathan.modern_design.user.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@WebAdapter
@RequestMapping("/api/v1/users")
class Controller {
    private final UserApi userApi;

    @GetMapping("/{id}")
    private UserDto getUser(@PathVariable UUID id) {
        return userApi.findUser(new User.UserId(id));
    }
}

@Slf4j
@RequiredArgsConstructor
public class UserFinder {
    private final UserRepo userRepo;

    public UserDto findUser(User.UserId userId) {
        log.info("BEGIN FindUser");
        final var user = userRepo.findByUUIDOrElseThrow(userId);

        //TODO var userResource = UserResource.from(user);
        var userResource = new UserDto("a", "a", "a", "a", null);

        log.info("END FindUser");
        return userResource;
    }
}
