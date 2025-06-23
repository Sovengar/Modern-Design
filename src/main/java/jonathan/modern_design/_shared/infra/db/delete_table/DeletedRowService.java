package jonathan.modern_design._shared.infra.db.delete_table;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jonathan.modern_design._config.exception.RootException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeletedRowService {
    private final DeletedRowRepository deletedRowRepository;
    private final ObjectMapper objectMapper;

    public <T> void saveDeletedEntity(T entity, String originTable, String originId, String deletedBy, String reason) {

        try {
            var data = objectMapper.writeValueAsString(entity);
            var deletedRow = DeletedRow.of(originTable, originId, deletedBy, reason, data);
            deletedRowRepository.save(deletedRow);
        } catch (JsonProcessingException e) {
            throw new SerializingDeletedRowException("Error serializing deleted entity", e);
        }
    }

    public <T> Optional<T> getDeletedEntity(String originTable, String originId, Class<T> entityClass) {
        try {
            return deletedRowRepository.findByOriginTableAndOriginId(originTable, originId)
                    .map(deletedRow -> {
                        try {
                            return objectMapper.readValue(deletedRow.getData(), entityClass);
                        } catch (JsonProcessingException e) {
                            throw new SerializingDeletedRowException("Error deserializing deleted entity", e);
                        }
                    });
        } catch (Exception e) {
            throw new RetrievingDeletedRowException("Error fetching the deleted entity", e);
        }
    }

    static class SerializingDeletedRowException extends RootException {
        @Serial private static final long serialVersionUID = -1004327277967045216L;

        public SerializingDeletedRowException(String message, Throwable cause) {
            super(HttpStatus.INTERNAL_SERVER_ERROR, message, "", cause);
        }
    }

    static class RetrievingDeletedRowException extends RootException {
        @Serial private static final long serialVersionUID = 7317110813310101020L;

        public RetrievingDeletedRowException(String message, Throwable cause) {
            super(HttpStatus.INTERNAL_SERVER_ERROR, message, "", cause);
        }
    }
}
