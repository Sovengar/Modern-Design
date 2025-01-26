package com.jonathan.modern_design.config.database;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class FlywayConfig {

    @Value("${flyway.cleandb:false}")
    private boolean cleanDatabase;

    private final DataSource dataSource;
    private final Environment env;

    @Bean
    public Flyway flyway() {
        boolean isLocalProfile = env.acceptsProfiles(Profiles.of("local"));

        final var schema = "md";
        final var encoding = "UTF-8";

        Flyway flyway = isLocalProfile ?
                Flyway.configure()
                        .dataSource(dataSource)
                        .cleanDisabled(!cleanDatabase)
                        .locations("classpath:db/migrations", "classpath:db/dev_test/flyway")
                        .schemas(schema)
                        .encoding(encoding)
                        .baselineOnMigrate(true)
                        .validateOnMigrate(true)
                        .validateMigrationNaming(true)
                        .load() :
                Flyway.configure().dataSource(dataSource)
                        .cleanDisabled(!cleanDatabase)
                        .locations("classpath:db/migrations")
                        .schemas(schema)
                        .encoding(encoding)
                        .baselineOnMigrate(true)
                        .validateOnMigrate(true)
                        .validateMigrationNaming(true)
                        .load();

        if (cleanDatabase) {
            flyway.clean();
        }

        if (isLocalProfile) {
            flyway.migrate();
        }

        return flyway;
    }
}
