package jonathan.modern_design.amazon.order.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jonathan.modern_design.amazon.order.OrderStatus;
import jonathan.modern_design.amazon.order.OrderStatusChanged;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Getter(onMethod = @__(@JsonProperty))
@ToString
@Entity
@Table(name = "orders", schema = "orders")
@NoArgsConstructor(access = AccessLevel.PRIVATE) //For Hibernate
public class Order extends AbstractAggregateRoot<Order> {
    @Id
    @Column(name = "order_id")
    private UUID id;
    private LocalDate placedOn;
    @Setter
    @NotNull
    private String customerId;
    @Setter
    private String shippingAddress;
    private String shippingTrackingNumber;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @ElementCollection
    @CollectionTable(
            name = "order_items",
            schema = "orders",
            joinColumns = @JoinColumn(name = "order_id")
    )
    @Setter
    @NotNull
    @NotEmpty
    private Map<String, Integer> items = new HashMap<>();

    private Order(UUID id, LocalDate placedOn, String customerId, String shippingAddress, String shippingTrackingNumber, OrderStatus status, Map<String, Integer> items) {
        this.id = Objects.nonNull(id) ? id : UUID.randomUUID();
        this.placedOn = placedOn;
        this.customerId = customerId;
        this.shippingAddress = shippingAddress;
        this.shippingTrackingNumber = shippingTrackingNumber;
        this.status = status;
        this.items = items;

        this.registerEvent(new OrderPlaced(id));
    }

    public static Order place(UUID id, String customerId, String shippingAddress, String shippingTrackingNumber, Map<String, Integer> items) {
        return new Order(id, LocalDate.now(), customerId, shippingAddress, shippingTrackingNumber, OrderStatus.AWAITING_PAYMENT, items);
    }

    public Order pay(boolean ok) {
        status.requireOneOf(OrderStatus.AWAITING_PAYMENT);
        status = ok ? OrderStatus.PAYMENT_APPROVED : OrderStatus.PAYMENT_FAILED;
        // Magic: all events registered are published by Spring at repo.save(this)
        registerEvent(new OrderStatusChanged(id, status, customerId));
        return this;
    }

    public Order wasScheduleForShipping(String trackingNumber) {
        status.requireOneOf(OrderStatus.PAYMENT_APPROVED);
        status = OrderStatus.SHIPPING_IN_PROGRESS;
        shippingTrackingNumber = trackingNumber;
        registerEvent(new OrderStatusChanged(id, status, customerId));
        return this;
    }

    public void wasShipped(boolean ok) {
        status.requireOneOf(OrderStatus.SHIPPING_IN_PROGRESS);
        status = ok ? OrderStatus.SHIPPING_COMPLETED : OrderStatus.SHIPPING_FAILED;
        registerEvent(new OrderStatusChanged(id, status, customerId));
    }

}
