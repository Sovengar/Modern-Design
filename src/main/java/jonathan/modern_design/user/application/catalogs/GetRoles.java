package jonathan.modern_design.user.application.catalogs;

import jonathan.modern_design._common.annotations.DataAdapter;
import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design._common.api.Response;
import jonathan.modern_design.user.domain.Role;
import jonathan.modern_design.user.domain.repos.RoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@WebAdapter
@RequestMapping("/api/v1/roles")
class RolFinderController {
    private final GetRoles getRoles;

    @GetMapping
    public ResponseEntity<Response<List<String>>> getAll() {
        var roles = getRoles.findAll();

        //TODO VER SI HAGO UN DTO NI QUE SEA EN DOMAIN YA QUE NO PUEDO SEARIALIZAR VOs
        //NO DTO BECAUSE IS A 1:1 MAP, THERE IS NOTHING TO HIDE AND THE DTO AND MODEL EVOLVES TOGETHER
        Response<List<String>> response = new Response.Builder<List<String>>()
                .data(roles.stream().map(Role::description).toList())
                .metadata(Map.of(
                        "timestamp", Instant.now(),
                        "version", "1.0"
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
    private final GetRolQuery querier;
    private final RoleRepo roleRepo;

    public List<Role> findAll() {
        var abc = querier.findAll();
        return roleRepo.findAll();
    }

}

@DataAdapter
class GetRolQuery {

    public List<Role> findAll() {
        return null;
    }
}
