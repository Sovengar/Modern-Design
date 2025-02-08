package com.jonathan.modern_design;


import com.jonathan.modern_design.config.TestContainersConfig;
import org.springframework.boot.SpringApplication;

public class TestModernDesignApplication {
    public static void main(String[] args) {
        SpringApplication.from(AppRuner::main).with(TestContainersConfig.class).run(args);
    }
}
