package jonathan.modern_design._shared.infra.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import jonathan.modern_design._shared.domain.vo.URLStringed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class URLListToStringConverter implements AttributeConverter<List<URLStringed>, String> {
    private static final String SEPARATOR = ";";

    @Override
    public String convertToDatabaseColumn(List<URLStringed> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }

        return attribute.stream()
                .map(URLStringed::getValue)
                .collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public List<URLStringed> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(dbData.split(SEPARATOR))
                .map(URLStringed::of)
                .collect(Collectors.toList());
    }
}
