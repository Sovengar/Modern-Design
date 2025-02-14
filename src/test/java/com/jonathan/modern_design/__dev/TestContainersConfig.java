package com.jonathan.modern_design.__dev;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@TestConfiguration(proxyBeanMethods = false)
@Import({PostgreSQLConfig.class})
@ActiveProfiles("test")
public class TestContainersConfig {
    //Import the beans to use, like postgres, kafka, etc...
}
