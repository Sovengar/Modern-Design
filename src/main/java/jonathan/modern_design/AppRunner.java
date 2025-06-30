package jonathan.modern_design;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.modulith.Modulithic;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
// allow classes with the same name in different packages, e.g. "InitialData"
@SpringBootApplication //(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
@EnableJpaRepositories
@EnableJpaAuditing
@EnableTransactionManagement
@EnableFeignClients
@ConfigurationPropertiesScan
@Modulithic(sharedModules = "_shared") //For Integrated/Contract/e2e tests with @ApplicationModuleTest
public class AppRunner {

    static {
        log.info("AppRunner loaded");
    }

    public static void main(String[] args) {
        SpringApplication.run(AppRunner.class, args);
    }
}
