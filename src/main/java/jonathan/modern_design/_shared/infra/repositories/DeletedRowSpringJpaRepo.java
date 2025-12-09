package jonathan.modern_design._shared.infra.repositories;

import jonathan.modern_design._shared.domain.models.DeletedRow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeletedRowSpringJpaRepo extends JpaRepository<DeletedRow, Long> {
    List<DeletedRow> findByOriginTable(String originTable);

    Optional<DeletedRow> findByOriginTableAndOriginId(String originTable, String originId);
}
