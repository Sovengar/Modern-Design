package jonathan.modern_design.amazon.inventory.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepo extends JpaRepository<Stock, String> {
}

