package jonathan.modern_design._shared.tags.persistence;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * When there is no need to map the Entity Object to apply commands, we can dettach the object
 * using a primitive like a Long or Enum.
 * This acts as a lazy relation, making it easier doing commands and mapping.
 * Ideal for Aggregates
 */
public @interface LinkedAsFKinDB {
}
