package jonathan.modern_design.user.application.queries;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design.user.api.dtos.UserDto;
import jonathan.modern_design.user.domain.models.User;
import jonathan.modern_design.user.domain.store.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@WebAdapter("/api/v1/users")
class FindUserHttpController {
    private final FindUser querier;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable UUID id) {
        log.info("BEGIN Controller - FindUser");
        var user = querier.queryWith(User.Id.of(id));
        log.info("END Controller - FindUser");
        return ResponseEntity.ok().body(user);
    }
}

@Controller
@RequiredArgsConstructor
class FindUserGraphQLController {
    private final UserRepo userRepo;

    @QueryMapping
    public User userById(@Argument String id) {
        return userRepo.findByUUIDOrElseThrow(User.Id.of(UUID.fromString(id)));
    }

    @QueryMapping
    public List<User> users() {
        return userRepo.findAll();
    }
}

@Slf4j
@RequiredArgsConstructor
@Injectable
public class FindUser {
    private final UserRepo userRepo;

    public UserDto queryWith(User.Id userId) {
        log.info("BEGIN FindUser");
        final var user = userRepo.findByUUIDOrElseThrow(userId);
        var userResource = new UserDto(user);
        log.info("END FindUser");
        return userResource;
    }
}
