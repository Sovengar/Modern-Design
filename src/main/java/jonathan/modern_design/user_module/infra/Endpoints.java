package jonathan.modern_design.user_module.infra;

import jonathan.modern_design._internal.config.annotations.WebAdapter;
import jonathan.modern_design.user_module.UserApi;
import jonathan.modern_design.user_module.domain.User;
import jonathan.modern_design.user_module.dtos.UserResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

//Controller could be moved to facade since at this point is a middle-man antipattern

@Slf4j
@RequiredArgsConstructor
@WebAdapter
@RequestMapping("/api/v1/users")
class UserController {
    private final UserApi userApi;

    @GetMapping("/{id}")
    private UserResource getUser(@PathVariable UUID id) {
        return userApi.findUser(new User.UserId(id));
    }
}

@Slf4j
@RequiredArgsConstructor
@WebAdapter
@RequestMapping("/api/v1/roles")
class RoleController {
    private final UserApi userApi;

    @GetMapping
    private UserResource getAll() {
        //return userApi.findRoles();
        return null; //TODO
    }
}
