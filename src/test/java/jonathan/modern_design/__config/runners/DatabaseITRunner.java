package jonathan.modern_design.__config.runners;

import jonathan.modern_design.__config.details.DatabaseTags;
import jonathan.modern_design.__config.details.IntegrationTags;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@IntegrationTags
@DatabaseTags
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public @interface DatabaseITRunner {
    //Add @Import in your target class to import your slice.
    //Add @EnableTestContainers in your target class to enable testcontainers.
}
