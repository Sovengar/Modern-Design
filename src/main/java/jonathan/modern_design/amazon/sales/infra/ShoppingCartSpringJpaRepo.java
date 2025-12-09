package jonathan.modern_design.amazon.sales.infra;

import jonathan.modern_design.amazon.sales.domain.ShoppingCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShoppingCartSpringJpaRepo extends JpaRepository<ShoppingCartEntity, UUID> {
}
