package jonathan.modern_design.__config.shared_for_all_classes;

import jonathan.modern_design.__config.DatabaseConfig;
import jonathan.modern_design.__config.IntegrationConfig;
import jonathan.modern_design.__config.WebConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@IntegrationConfig
@WebConfig
@DatabaseConfig
public @interface AceptanceTest {
    //Add @SpringBoot or @SpringModulith in your test class with the configuration you need.
    //Add @EnableTestContainers in your test class to combine with testcontainers.
}
