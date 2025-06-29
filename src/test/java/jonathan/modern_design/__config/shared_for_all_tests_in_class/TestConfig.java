package jonathan.modern_design.__config.shared_for_all_tests_in_class;

import jonathan.modern_design.__config.PrettyTestNames;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Tag;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Tag("integration")
@DisplayNameGeneration(PrettyTestNames.class)
class TestConfig {
}
