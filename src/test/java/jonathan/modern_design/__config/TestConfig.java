package jonathan.modern_design.__config;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Tag;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Tag("integration")
@DisplayNameGeneration(PrettyTestNames.class)
class TestConfig {
}
