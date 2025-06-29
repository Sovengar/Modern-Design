package jonathan.modern_design.__config.shared_for_all_classes;

import jonathan.modern_design.__config.PrettyTestNames;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("test")
@DisplayNameGeneration(PrettyTestNames.class)
@ExtendWith(MockitoExtension.class)
public @interface UnitTest {
}
