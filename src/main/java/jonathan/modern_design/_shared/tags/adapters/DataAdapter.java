package jonathan.modern_design._shared.tags.adapters;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repository
public @interface DataAdapter {
    //Specifies that the class is an adapter of an abstract repository using a specific implementation/technology

    @AliasFor(annotation = Repository.class)
    String value() default "";

}
