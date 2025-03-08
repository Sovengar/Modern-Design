package com.jonathan.modern_design.__dev;


import com.jonathan.modern_design.AppRunner;
import org.springframework.boot.SpringApplication;

public class TestModernDesignApplication {
    public static void main(String[] args) {
        SpringApplication.from(AppRunner::main).with(TestContainersConfig.class).run(args);
    }
}
