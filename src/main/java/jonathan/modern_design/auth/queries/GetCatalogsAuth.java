package jonathan.modern_design.auth.queries;

import io.swagger.v3.oas.annotations.Operation;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.tags.adapters.WebAdapter;
import jonathan.modern_design.auth.domain.models.User;
import jonathan.modern_design.auth.domain.store.RoleStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

import static jonathan.modern_design._shared.infra.AppUrls.AuthUrls.AUTH_MODULE_URL;
import static jonathan.modern_design._shared.infra.AppUrls.AuthUrls.CATALOGS_URL;

@Slf4j
@RequiredArgsConstructor
@WebAdapter(AUTH_MODULE_URL + CATALOGS_URL)
class GetCatalogsAuth {
    private final RoleStore roleStore;

    @Operation(summary = "Get all roles")
    @GetMapping(value = "/roles")
    public ResponseEntity<Response<List<Row>>> getAll() {
        var roles = roleStore.findAll();
        var data = roles.stream().map(role -> new Row(role.getCode().getRoleCode(), role.getCode().getRoleCode(), role.getDescription())).toList();

        return ResponseEntity.ok().body(new Response.Builder<List<Row>>().data(data).withDefaultMetadataV1());
    }

    @Operation(summary = "Get all user status")
    @GetMapping(value = "/userStatus")
    public ResponseEntity<Response<List<Row>>> getUserStatus() {
        var data = Arrays.stream(User.UserStatus.values()).map(status -> new Row(status.name(), status.name(), status.name())).toList();
        return ResponseEntity.ok().body(new Response.Builder<List<Row>>().data(data).withDefaultMetadataV1());
    }

    public record Row(String key, String value, String description) {
    }
}
