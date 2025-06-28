package jonathan.modern_design.amazon.payment;

import jonathan.modern_design.amazon.payment.infra.PaymentGatewayClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentApi {
    private final PaymentGatewayClient paymentGatewayClient;

    public String generatePaymentUrl(UUID orderId, double total) {
        // payment gateway implementation details
        log.info("Request payment url for order id: " + orderId);
        String gatewayUrl = paymentGatewayClient.generatePaymentLink("order/" + orderId + "/payment-accepted", total, "modulith-app");
        return gatewayUrl + "&orderId=" + orderId;
    }
}
