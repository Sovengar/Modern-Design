package jonathan.modern_design.amazon.shipping.api;

import jonathan.modern_design.amazon.shipping.domain.StopEntity;
import jonathan.modern_design.amazon.shipping.domain.StopRepo;
import jonathan.modern_design.amazon.shipping.infra.ShippingProperties;
import jonathan.modern_design.amazon.shipping.infra.ShippingProviderClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingApi {
    private final ShippingService shippingService;

    public String requestShipment(UUID orderId, String customerAddress) {
        return shippingService.requestShipment(orderId, customerAddress);
    }
}

@Slf4j
@Service
@RequiredArgsConstructor
class ShippingService {
    private final ShippingProviderClient shippingProviderClient;
    private final ShippingProperties shippingProperties;
    private final StopRepo stopRepo = null;

    public String requestShipment(UUID orderId, String customerAddress) {
        log.info("Request shipping at " + customerAddress);
        return shippingProviderClient.requestShipment(shippingProperties.shippingFromAddress(), customerAddress, orderId);
    }

    public void handleArrival(UUID shipmentId, Long stopId) {
        List<StopEntity> stops = stopRepo.findStopDataByShipmentId(shipmentId);
        //TODO
        //var shipment = Shipment.Factory.create(stops);
        //shipment.arrive(stopId);
    }
}
