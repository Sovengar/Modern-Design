package com.jonathan.modern_design;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
//TODO PONER? @EnableTransactionManagement
public class AppRuner {
    public static void main(String[] args) {
        SpringApplication.run(AppRuner.class, args);
    }
}
