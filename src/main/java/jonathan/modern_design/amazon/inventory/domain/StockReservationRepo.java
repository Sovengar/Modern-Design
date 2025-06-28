package jonathan.modern_design.amazon.inventory.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockReservationRepo extends JpaRepository<StockReservation, Long> {
    void deleteAllByOrderId(long orderId);
}
