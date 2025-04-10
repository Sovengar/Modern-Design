package jonathan.modern_design.user.infra;

import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design.user.UserApi;
import jonathan.modern_design.user.domain.User;
import jonathan.modern_design.user.dtos.UserDto;
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
    private UserDto getUser(@PathVariable UUID id) {
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
    private UserDto getAll() {
        //return userApi.findRoles();
        return null; //TODO
    }
}
