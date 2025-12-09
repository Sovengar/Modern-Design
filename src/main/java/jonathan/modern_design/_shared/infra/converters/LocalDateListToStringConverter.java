package jonathan.modern_design._shared.infra.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//
@Converter
public class LocalDateListToStringConverter implements AttributeConverter<List<LocalDate>, String> {
    private static final String SEPARATOR = ";";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public String convertToDatabaseColumn(List<LocalDate> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }

        return attribute.stream()
                .map(date -> date.format(FORMATTER))
                .collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public List<LocalDate> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(dbData.split(SEPARATOR))
                .map(dateStr -> LocalDate.parse(dateStr.trim(), FORMATTER))
                .toList();
    }
}
