package jonathan.modern_design.__config.shared_for_all_tests_in_class;

import jonathan.modern_design.__config.DatabaseConfig;
import jonathan.modern_design.__config.IntegrationConfig;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static jonathan.modern_design._config.database.FlywayConfig.SCHEMAS;

@IntegrationConfig
@DataJpaTest
@DatabaseConfig
@Testcontainers
public abstract class RepositoryITConfig {

    @Container
    @ServiceConnection
    protected static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

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
