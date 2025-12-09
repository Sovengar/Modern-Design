package jonathan.modern_design.auth.queries;

import io.swagger.v3.oas.annotations.Operation;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.tags.Injectable;
import jonathan.modern_design._shared.tags.adapters.WebAdapter;
import jonathan.modern_design.auth.api.dtos.UserDto;
import jonathan.modern_design.auth.domain.models.User;
import jonathan.modern_design.auth.domain.store.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

import static jonathan.modern_design._shared.infra.AppUrls.AuthUrls.AUTH_MODULE_URL;
import static jonathan.modern_design._shared.infra.AppUrls.AuthUrls.USER_RESOURCE_URL;
import static jonathan.modern_design._shared.infra.TraceIdGenerator.generateTraceId;

@Slf4j
@RequiredArgsConstructor
@WebAdapter(AUTH_MODULE_URL + USER_RESOURCE_URL)
//Generic FindUser, Implicit. Who calls this? For what? Cannot do HATEOAS if this is generic.
class FindUserHttpController {
    private final FindUser querier;

    @Operation(summary = "Find a auth by id")
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
