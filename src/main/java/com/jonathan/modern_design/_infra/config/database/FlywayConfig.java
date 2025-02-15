package com.jonathan.modern_design._infra.config.database;

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

    private final static String SCHEMA = "md";
    private final static String ENCODING = "UTF-8";
    private final DataSource dataSource;
    private final Environment env;
    @Value("${flyway.cleandb:false}")
    private boolean cleanDatabase;

    @Bean
    public Flyway flyway() {
        boolean isLocalProfile = env.acceptsProfiles(Profiles.of("local"));

        final String[] migrationLocations = isLocalProfile ? new String[]{"classpath:db/migrations", "classpath:db/dev"} : new String[]{"classpath:db/migrations"};

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .cleanDisabled(!cleanDatabase)
                .locations(migrationLocations)
                .schemas(SCHEMA)
                .encoding(ENCODING)
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
