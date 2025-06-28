package jonathan.modern_design.amazon.inventory.internal.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE) //For Hibernate
@Table(name = "stock_reservations", schema = "inventory")
@SequenceGenerator(name = "stock_reservations_sq", schema = "inventory", allocationSize = 1)
public class StockReservation {
    @Id
    @GeneratedValue(generator = "stock_reservations_sq")
    @Column(name = "reservation_id")
    private Long id;

    @NotNull
    private Long orderId;

    @NotNull
    private String productId;

    @NotNull
    private Integer quantityOnHand;

    private LocalDateTime createdAt = now();

    public StockReservation(Long orderId, String productId, Integer quantityOnHand) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantityOnHand = quantityOnHand;
    }
}
