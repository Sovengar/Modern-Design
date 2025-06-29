package jonathan.modern_design.amazon.payment.infra;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("payment-gateway")
public interface PaymentGatewayClient {
    @GetMapping("get-payment-link")
    String generatePaymentLink(@RequestParam String redirectUrl,
                               @RequestParam Double total,
                               @RequestParam String clientApp);
}
