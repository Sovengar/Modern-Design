package jonathan.modern_design.__config;

import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;

// use via @DisplayNameGeneration on a test class
public class PrettyTestNames extends DisplayNameGenerator.Standard {
    private PrettyTestNames() {
    }

    @Override
    public String generateDisplayNameForClass(Class<?> testClass) {
        return replaceCapitals(super.generateDisplayNameForClass(testClass));
    }

    @Override
    public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
        return replaceCapitals(super.generateDisplayNameForNestedClass(nestedClass));
    }

    @Override
    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        return replaceCapitals(testMethod.getName());
    }

    private String replaceCapitals(String name) {
        name = name.replaceAll("([A-Z])", " $1")
                .replaceAll("_", " ")
                .replaceAll("\\s+", " ")
                .toLowerCase();
        return name;
    }
}
