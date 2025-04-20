package jonathan.modern_design.user.application;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design.user.api.dtos.UserDto;
import jonathan.modern_design.user.domain.User;
import jonathan.modern_design.user.domain.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@WebAdapter
@RequestMapping("/api/v1/users")
class FindUserController {
    private final FindUser querier;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable UUID id) {
        log.info("BEGIN Controller - FindUser");
        var user = querier.queryWith(new User.UserId(id));
        log.info("END Controller - FindUser");
        return ResponseEntity.ok().body(user);
    }
}

@Slf4j
@RequiredArgsConstructor
@Injectable
public class FindUser {
    private final UserRepo userRepo;

    public UserDto queryWith(User.UserId userId) {
        log.info("BEGIN FindUser");
        final var user = userRepo.findByUUIDOrElseThrow(userId);
        var userResource = new UserDto(user);
        log.info("END FindUser");
        return userResource;
    }
}
