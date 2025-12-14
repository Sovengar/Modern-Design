package jonathan.modern_design.amazon.order;


import jonathan.modern_design.__config.runners.AcceptanceITRunner;
import jonathan.modern_design.__config.utils.EnableTestContainers;
import jonathan.modern_design.amazon.inventory.domain.LineItem;
import jonathan.modern_design.amazon.order.application.PlaceOrder;
import jonathan.modern_design.amazon.order.domain.Order;
import jonathan.modern_design.amazon.order.domain.OrderCompleted;
import jonathan.modern_design.amazon.order.domain.OrderPlaced;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.test.EnableScenarios;
import org.springframework.modulith.test.Scenario;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/// Should be @ApplicationModuleTest
@SpringBootTest
@EnableScenarios
///
@AcceptanceITRunner
@EnableTestContainers
class OrderIT {
    @Autowired
    private OrderApi orderApi;

    @Test
    void should_publish_order_placed_event(Scenario scenario) {
        var orderId = UUID.randomUUID();
        var placeOrderRequest = new PlaceOrder.PlaceOrderRequest(
                orderId,
                "123",
                List.of(new LineItem("1", 1)),
                "address"
        );

        scenario.stimulate(() -> orderApi.placeOrder(placeOrderRequest))
                .andWaitForEventOfType(OrderPlaced.class)
                .matchingMappedValue(OrderPlaced::orderId, orderId)
                .toArrive();
    }

    @Test
    void should_publish_order_completed_event(Scenario scenario) {
        var orderId = UUID.randomUUID();
        var order = Order.place(orderId, "123", "address", "111", Map.of());

        scenario.stimulate(() -> orderApi.complete(order))
                .andWaitForEventOfType(OrderCompleted.class)
                .matchingMappedValue(OrderCompleted::orderId, orderId)
                .toArrive();
    }
}
