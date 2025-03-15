package jonathan.modern_design._internal.web;

import jonathan.modern_design._internal.config.annotations.WebAdapter;
import jonathan.modern_design.user_module.domain.RoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@RequiredArgsConstructor
@WebAdapter
@RequestMapping("/api/v1/test")
class TestController {
    private final RoleRepo repo;

    @GetMapping
    String a() {
        return "holaaaa";
    }
}
