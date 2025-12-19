package jonathan.modern_design;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
// allow classes with the same name in different packages, e.g. "InitialData"
@SpringBootApplication //(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
//Avoid adding any config annotation here, use AppConfig.java instead!!!!!!!!!!!!!!!!!!!!!
public class AppRunner {

    static {
        log.info("AppRunner loaded");
    }

    public static void main(String[] args) {
        SpringApplication.run(AppRunner.class, args);
    }
}
