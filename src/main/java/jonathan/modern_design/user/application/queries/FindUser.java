package jonathan.modern_design.user.application.queries;

import io.swagger.v3.oas.annotations.Operation;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.tags.Injectable;
import jonathan.modern_design._shared.tags.WebAdapter;
import jonathan.modern_design.user.api.dtos.UserDto;
import jonathan.modern_design.user.domain.models.User;
import jonathan.modern_design.user.domain.store.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

import static jonathan.modern_design._shared.TraceIdGenerator.generateTraceId;


@Slf4j
@RequiredArgsConstructor
@WebAdapter("/v1/users")
//Generic FindUser, Implicit. Who calls this? For what? Cannot do HATEOAS if this is generic.
class FindUserHttpController {
    private final FindUser querier;

    @Operation(summary = "Find a user by id")
    @GetMapping("/{id}")
    public ResponseEntity<Response<UserDto>> getUser(@PathVariable UUID id) {
        Assert.state(StringUtils.hasText(String.valueOf(id)), "UserId is required");
        generateTraceId();

        log.info("BEGIN FindUser for userId: {}", id);
        var user = querier.queryWith(User.Id.of(id));
        log.info("END FindUser for userId: {}", id);

        return ResponseEntity.ok().body(new Response.Builder<UserDto>().data(user).withDefaultMetadataV1());
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
        var userResource = new UserDto(user, true);
        log.info("END FindUser");
        return userResource;
    }
}
