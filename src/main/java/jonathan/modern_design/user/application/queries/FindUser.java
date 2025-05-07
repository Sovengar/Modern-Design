package jonathan.modern_design.user.application.queries;

import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import jonathan.modern_design._common.tags.Injectable;
import jonathan.modern_design._common.tags.WebAdapter;
import jonathan.modern_design.user.api.dtos.UserDto;
import jonathan.modern_design.user.domain.models.User;
import jonathan.modern_design.user.domain.store.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

import static jonathan.modern_design._common.TraceIdGenerator.generateTraceId;


@Slf4j
@RequiredArgsConstructor
@WebAdapter("/api/v1/users")
class FindUserHttpController {
    private final FindUser querier;

    @Observed(name = "findUser")
    @Operation(summary = "Find a user by id")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable UUID id) {
        Assert.state(StringUtils.hasText(String.valueOf(id)), "UserId is required");
        generateTraceId();

        log.info("BEGIN FindUser for userId: {}", id);
        var user = querier.queryWith(User.Id.of(id));
        log.info("END FindUser for userId: {}", id);

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
