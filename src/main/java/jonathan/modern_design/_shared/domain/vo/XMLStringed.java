package jonathan.modern_design._shared.domain.vo;

import jakarta.persistence.Embeddable;
import jonathan.modern_design._shared.tags.models.ValueObject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static java.util.Objects.isNull;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Embeddable
@ValueObject
@Value
@NoArgsConstructor(access = PRIVATE, force = true) //For Hibernate
@AllArgsConstructor(access = PACKAGE) //Use factory method
public class XMLStringed {
    private String value;

    public static XMLStringed of(String value) {
        if (isNull(value) || value.trim().isEmpty()) {
            throw new IllegalArgumentException("value must not be empty");
        }

        if (!isValidXml(value)) {
            throw new IllegalArgumentException("The provided string is not valid XML");
        }

        return new XMLStringed(value.trim());
    }

    private static boolean isValidXml(String xml) {
//        try {
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            // Security: disable external entity resolution (prevent XXE)
//            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
//            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
//            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
//            factory.setXIncludeAware(false);
//            factory.setExpandEntityReferences(false);
//
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            builder.parse(new InputSource(new StringReader(xml)));
//            return true;
//        } catch (ParserConfigurationException | SAXException | IOException ex) {
//            return false;
//        }
        String trimmed = xml.trim();
        return trimmed.startsWith("<") && trimmed.endsWith(">");
    }
}
