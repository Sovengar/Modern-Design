package jonathan.modern_design._internal.config.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import jonathan.modern_design._common.annotations.MicroType;
import jonathan.modern_design._common.annotations.ValueObject;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.Serial;

@Configuration
class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.setSerializerModifier(buildSerializerModifier());

        mapper.registerModule(module);

        return mapper;
    }

    private BeanSerializerModifier buildSerializerModifier() {
        // Add a custom serializer for Value Objects that flattens their properties into the root object
        return new BeanSerializerModifier() {
            @Serial private static final long serialVersionUID = 4293029694831935791L;

            @Override
            public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                if (serializer instanceof BeanSerializer bs && isValueObjectOrMicroType(beanDesc.getBeanClass())) {
                    return new FlatteningSerializer(bs);
                }
                return serializer;
            }

            private boolean isValueObjectOrMicroType(Class<?> clazz) {
                return ValueObject.class.isAssignableFrom(clazz) || MicroType.class.isAssignableFrom(clazz);
            }
        };
    }
}

class FlatteningSerializer extends BeanSerializer {
    @Serial private static final long serialVersionUID = 922038601302742623L;

    public FlatteningSerializer(BeanSerializer src) {
        super(src);
    }

    protected FlatteningSerializer(BeanSerializerBase src, ObjectIdWriter objectIdWriter, Object filterId) {
        super(src, objectIdWriter, filterId);
    }

    @SneakyThrows
    @Override
    public void serialize(Object bean, JsonGenerator gen, SerializerProvider provider) {
        // Si hay solo una propiedad, serializamos directamente su valor
        if (_props != null && _props.length == 1) {
            Object value = _props[0].get(bean);
            if (value != null) {
                provider.defaultSerializeValue(value, gen);
                return;
            }
        }

        // Si hay más de una propiedad o el valor es null, usamos la serialización normal
        super.serialize(bean, gen, provider);
    }

    @Override
    public BeanSerializerBase withObjectIdWriter(ObjectIdWriter objectIdWriter) {
        return new FlatteningSerializer(this, objectIdWriter, _propertyFilterId);
    }

    @Override
    public BeanSerializerBase withFilterId(Object filterId) {
        return new FlatteningSerializer(this, _objectIdWriter, filterId);
    }
}

