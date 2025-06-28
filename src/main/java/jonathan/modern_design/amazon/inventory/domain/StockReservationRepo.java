package jonathan.modern_design.amazon.inventory.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StockReservationRepo extends JpaRepository<StockReservation, Long> {
    void deleteAllByOrderId(UUID orderId);
}
