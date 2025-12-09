package jonathan.modern_design._shared.tags;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
@Validated
/**
 * A facade is a design pattern that provides a simplified interface to a complex subsystem.
 * In the context of a software application, a facade can be used to encapsulate the complexity
 * of a set of related classes or modules, providing a single entry point for clients to interact
 * with the subsystem.
 */
public @interface Facade {

    @AliasFor(annotation = Component.class)
    String value() default "";
}
