package jonathan.modern_design.amazon.shipping.infra;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient("shipping-provider")
public interface ShippingProviderClient {
    @PostMapping("create-shipping")
    String requestShipment(@RequestParam String pickupAddress,
                           @RequestParam String deliveryAddress,
                           @RequestParam UUID orderId);
}
