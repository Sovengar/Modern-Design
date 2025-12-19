package jonathan.modern_design;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.modulith.Modulithic;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories
@EnableJpaAuditing
@EnableTransactionManagement
@EnableFeignClients
@ConfigurationPropertiesScan
@Modulithic(sharedModules = "_shared") //For Integrated/Contract/e2e tests with @ApplicationModuleTest
class AppConfig {
    //These annotations cannot be with @SpringBootApplication,
    //When using slices or @SpringModuleTest (a module slice in the end), Spring can have issues loading the context, loading it from your class containing @SpringBootApplication.
    //If the class that has @SpringBootApplication has other annotations like @EnableJpaRepositories or @EnableFeignClients, it will fetch them even in slice testing.
}
