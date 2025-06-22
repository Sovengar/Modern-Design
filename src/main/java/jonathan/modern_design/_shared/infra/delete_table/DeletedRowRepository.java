package jonathan.modern_design._shared.infra.delete_table;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeletedRowRepository extends JpaRepository<DeletedRow, Long> {
    List<DeletedRow> findByOriginTable(String originTable);

    Optional<DeletedRow> findByOriginTableAndOriginId(String originTable, String originId);
}
