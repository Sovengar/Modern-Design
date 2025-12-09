package jonathan.modern_design._shared.tags.persistence;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * Small catalogs that are used only by one entity
 * Which means they are ideal to be in memory only rather than being persisted in physical space (Database).
 * Furthermore, they are not expected to change frequently or to be manipulated from scripts or UI.
 * An example would be user role, document types... You are not supposed to add new roles from the UI because the code needs changes too.
 * An example of a non-candidate would be a catalog of countries, which is expected to change more frequently and to be managed from outside the code.
 */
/**
 * Another example is structs that for size or rapid development only have inMemoryRepository.
 */
public @interface InMemoryOnlyCatalog {
}
