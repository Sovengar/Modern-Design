package jonathan.modern_design;

import jonathan.modern_design.__config.initializers.InfraInitializer;
import jonathan.modern_design.amazon.order.OrderApi;
import jonathan.modern_design.amazon.order.domain.Order;
import jonathan.modern_design.amazon.order.domain.OrderCompleted;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.modulith.events.core.DefaultEventPublicationRegistry;
import org.springframework.modulith.events.core.EventPublicationRegistry;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for the overall application.
 */
@SpringBootTest
@InfraInitializer
@Import(EventPublicationRegistryTests.FailingAsyncTransactionalEventListener.class)
@DirtiesContext
@RequiredArgsConstructor
class EventPublicationRegistryTests {
    private final OrderApi orderApi;
    private final EventPublicationRegistry registry;
    private final FailingAsyncTransactionalEventListener listener;
    private final DefaultEventPublicationRegistry defaultRegistry;

    @Test
    void bootstrapsApplication(Scenario scenario) {
        //TODO END WHEN WE DO INVENTORY LOGIC
        //var placeOrderRequest = new PlaceOrder.PlaceOrderRequest(
        //        UUID.randomUUID(),
        //        "123",
        //        List.of(new LineItem("1", 1)),
        //        "address"
        //);
        //
        //scenario.stimulate(() -> orderApi.placeOrder(placeOrderRequest))
        //        .andWaitForStateChange(registry::findIncompletePublications)
        //        .andExpect(InventoryUpdated.class) //Event from another Bounded Context
        //        .toArrive();
    }

    @Test
    /*
      A showcase for how the Spring Modulith application event publication registry keeps track of incomplete publications
      for failing transactional event listeners
     */
    void leavesPublicationIncompleteForFailingListener(Scenario scenario) throws Exception {
        var order = Order.place(UUID.randomUUID(), "", "", "", null);

        scenario.stimulate(() -> orderApi.complete(order))
                .andWaitForStateChange(listener::getEx)
                .andVerify((Exception __) -> assertThat(registry.findIncompletePublications())
                        .hasSize(1));
    }

    static class FailingAsyncTransactionalEventListener {
        @Getter Exception ex;

        @ApplicationModuleListener
        void foo(OrderCompleted event) {

            var exception = new IllegalStateException("¯\\_(ツ)_/¯");

            try {
                throw exception;
            } finally {
                this.ex = exception;
            }
        }
    }
}
