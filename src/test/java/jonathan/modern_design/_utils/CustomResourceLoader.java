package jonathan.modern_design._utils;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class CustomResourceLoader {
    private final ResourceLoader resourceLoader;

    public String loadJsonFromClasspath(String relativePath) throws IOException {
        try (InputStream inputStream = resourceLoader.getResource("classpath:" + relativePath).getInputStream()) {
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        }
    }
}

