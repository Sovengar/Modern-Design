package jonathan.modern_design.__config.shared_for_all_tests_in_class;

import jonathan.modern_design.__config.DatabaseConfig;
import jonathan.modern_design.__config.IntegrationConfig;
import jonathan.modern_design.__config.WebConfig;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static jonathan.modern_design._config.database.FlywayConfig.SCHEMAS;

@SpringBootTest
@IntegrationConfig
@DatabaseConfig
@WebConfig
@Testcontainers
public abstract class ITConfig {
    @Container
    @ServiceConnection
    protected static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"))
            .withReuse(true); // Ensure that testcontainers.reuse.enable=true is set in ~/.testcontainers.properties
    //TODO DOUBT WITH REUSE

    @Autowired
    protected MockMvc mockMvc;

    @BeforeAll
    public static void beforeAll() {
        Flyway flyway = Flyway.configure().dataSource(
                        postgres.getJdbcUrl(),
                        postgres.getUsername(),
                        postgres.getPassword()
                )
                .locations("db/migrations")
                .schemas(SCHEMAS)
                .load();
        flyway.migrate();
    }
}
