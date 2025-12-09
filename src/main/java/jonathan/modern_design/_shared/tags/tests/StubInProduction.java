package jonathan.modern_design._shared.tags.tests;

import org.springframework.context.annotation.Primary;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Primary
public @interface StubInProduction {
    //Clarifies that the stub is being used in production for beta testing
    //This is possible because with @Primary we override other beans that are also implementing the same interface
}
