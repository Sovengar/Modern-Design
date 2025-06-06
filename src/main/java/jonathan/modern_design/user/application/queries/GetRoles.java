package jonathan.modern_design.user.application.queries;

import io.swagger.v3.oas.annotations.Operation;
import jonathan.modern_design._common.api.Response;
import jonathan.modern_design._common.tags.Injectable;
import jonathan.modern_design._common.tags.WebAdapter;
import jonathan.modern_design.user.api.dtos.RoleDto;
import jonathan.modern_design.user.domain.models.Role;
import jonathan.modern_design.user.domain.store.RoleStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/v1/roles")
class GetRolesController {
    private final GetRoles getRoles;

    @Operation(summary = "Get all roles")
    @GetMapping
    public ResponseEntity<Response<List<RoleDto>>> getAll() {
        var roles = getRoles.findAll();
        var roleDtos = roles.stream().map(RoleDto::new).toList();

        return ResponseEntity.ok().body(new Response.Builder<List<RoleDto>>().data(roleDtos).withDefaultMetadataV1());
    }
}

@Slf4j
@RequiredArgsConstructor
@Injectable
class GetRoles {
    private final RoleStore roleStore;

    public List<Role> findAll() {
        return roleStore.findAll();
    }
}
