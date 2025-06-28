package jonathan.modern_design.amazon.shipping;

import jonathan.modern_design.amazon.shipping.api.ShippingResultResolved;
import jonathan.modern_design.amazon.shipping.application.AckShipping;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.PublishedEvents;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationModuleTest
class ShippingModuleTest {
    @Autowired
    AckShipping sut;

    UUID orderId = UUID.randomUUID();

    @Test
    void callback(PublishedEvents publishedEvents) {
        sut.call(orderId, true);

        assertThat(publishedEvents.ofType(ShippingResultResolved.class))
                .containsExactly(new ShippingResultResolved(orderId, true));
    }
}
