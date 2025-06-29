package jonathan.modern_design.__config.shared_for_all_tests_in_class;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class WebITConfig extends TestConfig {
    @Autowired
    protected WebApplicationContext webApplicationContext;
    @Autowired
    protected MockMvc mockMvc;
}
