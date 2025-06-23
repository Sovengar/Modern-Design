package jonathan.modern_design._shared.infra.config.modulith;

import org.springframework.context.annotation.Configuration;
import org.springframework.modulith.ApplicationModuleInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@EnableScheduling
@EnableAsync
class ModulithConfig implements ApplicationModuleInitializer {

    @Override
    public void initialize() {
        //System.setProperty("spring.modulith.events.default-publisher", "persistent");
    }
}
