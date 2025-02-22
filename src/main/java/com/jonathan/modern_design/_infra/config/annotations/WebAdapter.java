package com.jonathan.modern_design._infra.config.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@RestController
public @interface WebAdapter {

    //@AliasFor(annotation = RestController.class)
    String value() default "";

}
