package jonathan.modern_design;


import jonathan.modern_design.__config.dev.TestContainersConfig;
import org.springframework.boot.SpringApplication;

public class AppRunnerWithTestContainers {
    public static void main(String[] args) {
        SpringApplication.from(AppRunner::main).with(TestContainersConfig.class).run(args);
    }
}
