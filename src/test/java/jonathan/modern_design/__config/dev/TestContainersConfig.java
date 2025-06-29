package jonathan.modern_design.__config.dev;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration(proxyBeanMethods = false)
@Import({PostgreSQLConfig.class}) //Import the beans to use, like postgres, kafka, etc...
public class TestContainersConfig {
}
