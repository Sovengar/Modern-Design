package jonathan.modern_design;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@EnableJpaRepositories // (repositoryFactoryBeanClass =
@EnableJdbcRepositories
@EnableJpaAuditing
@EnableTransactionManagement
public class AppRunner {

    static {
        log.info("AppRunner loaded");
    }

    public static void main(String[] args) {
        SpringApplication.run(AppRunner.class, args);
    }
}
