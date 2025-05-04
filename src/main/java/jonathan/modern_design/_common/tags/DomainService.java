package jonathan.modern_design._common.tags;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface DomainService {

    @AliasFor(annotation = Component.class)
    String value() default "";

    //DomainServices can reside in a slice, if it grows too complex or needs to be reused should be moved to domain/services
}
