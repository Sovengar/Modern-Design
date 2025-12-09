package jonathan.modern_design.__config.shared_for_all_classes;

import jonathan.modern_design.__config.DatabaseConfig;
import jonathan.modern_design.__config.IntegrationConfig;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@IntegrationConfig
@DatabaseConfig
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public @interface DatabaseTest {
    //Add @Import in your target class to import your slice.
    //Add @EnableTestContainers in your target class to enable testcontainers.
}
