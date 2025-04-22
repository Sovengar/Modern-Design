package jonathan.modern_design._internal.web;

import jonathan.modern_design._common.annotations.WebAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;


@Slf4j
@RequiredArgsConstructor
@WebAdapter("/api/v1/test")
class TestController {

    @GetMapping
    String a() {
        return "holaaaa";
    }
}
