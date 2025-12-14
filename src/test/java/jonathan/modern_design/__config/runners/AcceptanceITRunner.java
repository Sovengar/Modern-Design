package jonathan.modern_design.__config.runners;

import jonathan.modern_design.__config.details.DatabaseTags;
import jonathan.modern_design.__config.details.IntegrationTags;
import jonathan.modern_design.__config.details.WebTags;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@IntegrationTags
@WebTags
@DatabaseTags
public @interface AcceptanceITRunner {
    //Add @SpringBoot or @SpringModulith in your test class with the configuration you need.
    //Add @EnableTestContainers in your test class to combine with testcontainers.
}
