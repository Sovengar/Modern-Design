package jonathan.modern_design.__config;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Tag;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Tag("integration")
@ActiveProfiles("test")
@DisplayNameGeneration(PrettyTestNames.class)
@WebConfig
@DatabaseConfig
public @interface IntegrationConfig {
}




