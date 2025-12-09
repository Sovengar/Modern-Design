package jonathan.modern_design.banking.infra.store.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import jonathan.modern_design.banking.domain.vo.AccountHolderAddress;

@Converter
public class StreetTypeToCodeConverter implements AttributeConverter<AccountHolderAddress.StreetType, String> {

    @Override
    public String convertToDatabaseColumn(AccountHolderAddress.StreetType attribute) {
        return (attribute != null) ? attribute.getCode() : null;
    }

    @Override
    public AccountHolderAddress.StreetType convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        for (AccountHolderAddress.StreetType g : AccountHolderAddress.StreetType.values()) {
            if (g.getCode().equals(dbData)) {
                return g;
            }
        }
        throw new IllegalArgumentException("Código desconocido: " + dbData);
    }
}
