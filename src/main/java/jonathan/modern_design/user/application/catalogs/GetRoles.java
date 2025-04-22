package jonathan.modern_design.user.application.catalogs;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design._common.api.Response;
import jonathan.modern_design.user.api.dtos.UserDto;
import jonathan.modern_design.user.domain.models.Role;
import jonathan.modern_design.user.domain.store.RoleStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/api/v1/roles")
class RolFinderController {
    private final GetRoles getRoles;

    @GetMapping
    public ResponseEntity<Response<List<UserDto.RoleDto>>> getAll() {
        var roles = getRoles.findAll();

        Response<List<UserDto.RoleDto>> response = new Response.Builder<List<UserDto.RoleDto>>()
                .data(roles.stream().map(UserDto.RoleDto::new).toList())
                .metadata(Map.of(
                        "timestamp", Instant.now(),
                        "version", "1.0.0"
                ))
                //.links(List.of(
                //      new Response.Link("self", "/roles/" + id, "GET"),
                //    new Response.Link("all", "/roles", "GET")
                //))
                //.actions(List.of(
                //      new Response.Action("delete", "DELETE", "/roles/" + id),
                //    new Response.Action("update", "PUT", "/roles/" + id,
                //          Map.of("name", "string", "description", "string"))
                //))
                .build();

        return ResponseEntity.ok().body(response);
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
