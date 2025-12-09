package jonathan.modern_design.amazon.order.queries;

import jonathan.modern_design._shared.tags.adapters.WebAdapter;
import jonathan.modern_design.amazon.order.domain.Order;
import jonathan.modern_design.amazon.order.domain.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

import static jonathan.modern_design._shared.infra.AppUrls.AmazonUrls.OrderUrls.ORDERS_RESOURCE_URL;
import static jonathan.modern_design._shared.infra.AppUrls.AmazonUrls.OrderUrls.ORDER_MODULE_URL;

@WebAdapter(ORDER_MODULE_URL + ORDERS_RESOURCE_URL)
@RequiredArgsConstructor
public class GetOrder {
    private final OrderRepo orderRepo;

    @GetMapping("/{orderId}")
    public Order call(@PathVariable UUID orderId) {
        return orderRepo.findById(orderId).orElseThrow();
    }
}
