package com.jonathan.modern_design.config;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

//Option 1 Load exclusively context from TestContainersConfig
//@SpringBootTest(classes = {TestContainersConfig.class})
//Option 2 Load all context + TestContainersConfig context (beans)
@SpringBootTest
@Import(TestContainersConfig.class)

/*
En vez de ejecutar @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
Utilizamos @SpringBootTest+@AutoconfigureMockMvc pero seguimos utilizando el cliente que queramos.
Esto es porque lo ejecuta en 2 threads, por tanto 2 transacciones, haciendo que no funcione el rollback
*/
@AutoConfigureMockMvc
//////////////////////////

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Testcontainers
@Transactional
@Rollback
@Tag("integration")
public abstract class IntegrationTestConfig {

    @Container
    @ServiceConnection
    protected static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15.7"));
    @Autowired
    protected WebApplicationContext webApplicationContext;
    protected MockMvc mockMvc;

    @BeforeAll
    static void beforeAll() {
        Flyway flyway = Flyway.configure().dataSource(
                        postgres.getJdbcUrl(),
                        postgres.getUsername(),
                        postgres.getPassword()
                )
                .locations("db/migrations")
                .schemas("db")
                .load();
        flyway.migrate();
    }

    @BeforeEach
    void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }
}
