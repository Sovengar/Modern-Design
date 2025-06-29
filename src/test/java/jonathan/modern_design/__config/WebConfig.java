package jonathan.modern_design.__config;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@AutoConfigureMockMvc
//Better than @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) to avoid 2 threads which means 2 transactions
public @interface WebConfig {
}
