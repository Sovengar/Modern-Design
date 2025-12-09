package jonathan.modern_design._shared.tags.models;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * A Value Object is a small object that represents a descriptive aspect of the domain with no conceptual identity.
 * Its equality is determined by comparing its attributes rather than an identifier.
 * They are inmutable and interchangeable.
 *
 * An example: Imagine a cinema with seats. Each seat has: Row (e.g., "A") and Number (e.g., "10")
 * If you have two seats with the same row and number, they are the same seat, regardless of any internal ID. You don’t care about an identifier; you care about the position.
 *
 * IMPORTANT: A class with this annotation indicates that even if it is an entity, it should not have a repository
 */
public @interface ValueObject {
    /**
     * Indicates if the Value Object is also mapped as an Entity in the persistence layer.
     * Default is false.
     */
    boolean mappedEntity() default false;
}

