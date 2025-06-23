package jonathan.modern_design._shared.tags;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Fake {
//This class is for unit tests, also, don't evaluate his state, pointless, rather evaluate the state of the objects
}
