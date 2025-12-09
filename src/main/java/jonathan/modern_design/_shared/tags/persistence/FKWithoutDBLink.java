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
 * A primary key from another domain -> Database which means we don't (or should) have
 * a FK constraint in the DB level.
 */
public @interface FKWithoutDBLink {
}
