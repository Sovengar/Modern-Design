package com.jonathan.modern_design.__dev;


import com.jonathan.modern_design.AppRuner;
import org.springframework.boot.SpringApplication;

public class TestModernDesignApplication {
    public static void main(String[] args) {
        SpringApplication.from(AppRuner::main).with(TestContainersConfig.class).run(args);
    }
}
