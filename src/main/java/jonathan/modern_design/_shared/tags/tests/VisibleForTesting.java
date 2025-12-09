package jonathan.modern_design._shared.tags.tests;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface VisibleForTesting {
    //Clarifies that the method is public for testing purposes and production code should not consume it directly.
}
