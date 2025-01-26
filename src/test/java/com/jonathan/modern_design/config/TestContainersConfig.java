package com.jonathan.modern_design.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@TestConfiguration(proxyBeanMethods = false)
@Import({PostgreSQLConfig.class})
@ActiveProfiles("test")
public class TestContainersConfig {}
