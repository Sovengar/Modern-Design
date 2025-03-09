package com.jonathan.modern_design;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@EnableAutoConfiguration
public class AppRunner {

    static {
        log.info("AppRunner loaded");
    }

    public static void main(String[] args) {
        SpringApplication.run(AppRunner.class, args);
    }
}
