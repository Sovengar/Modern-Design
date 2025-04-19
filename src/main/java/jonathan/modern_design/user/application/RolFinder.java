package jonathan.modern_design.user.application;

import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design.user.UserApi;
import jonathan.modern_design.user.domain.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@WebAdapter
@RequestMapping("/api/v1/roles")
class RolFinder {
    private final UserApi userApi;

    @GetMapping
    private Role getAll() {
        return userApi.findAllRoles();
    }
}
