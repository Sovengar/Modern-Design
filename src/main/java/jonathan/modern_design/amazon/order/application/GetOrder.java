package jonathan.modern_design.amazon.order.application;

import jonathan.modern_design.amazon.order.domain.Order;
import jonathan.modern_design.amazon.order.domain.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class GetOrder {
    private final OrderRepo orderRepo;

    @GetMapping("order/{orderId}")
    public Order call(@PathVariable UUID orderId) {
        return orderRepo.findById(orderId).orElseThrow();
    }
}
