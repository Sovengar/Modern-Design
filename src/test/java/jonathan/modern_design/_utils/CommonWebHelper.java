package jonathan.modern_design._utils;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.jsonunit.core.Option;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.IOException;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;

@Component
@RequiredArgsConstructor
public final class CommonWebHelper {
    private final CustomResourceLoader customResourceLoader;

    public ResultMatcher validJson(final String path) throws IOException {
        return json().when(Option.IGNORING_EXTRA_FIELDS).isEqualTo(customResourceLoader.loadJsonFromClasspath(path));
    }

    public ResultMatcher validJsonIgnoringArrayOrder(final String path) throws IOException {
        return json().when(Option.IGNORING_EXTRA_FIELDS, Option.IGNORING_ARRAY_ORDER).isEqualTo(customResourceLoader.loadJsonFromClasspath(path));
    }


}
