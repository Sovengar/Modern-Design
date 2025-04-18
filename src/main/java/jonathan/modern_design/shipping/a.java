package jonathan.modern_design.shipping;

import jonathan.modern_design.shipping.domain.Shipment;
import jonathan.modern_design.shipping.domain.StopEntity;
import jonathan.modern_design.shipping.domain.StopStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

//interface StopRepository extends JpaRepository<StopEntity, Long> {
//    //@Query("SELECT new com.example.shipment.domain.StopDto(s.id, s.sequence, s.status) " +
//    //      "FROM StopEntity s WHERE s.shipment.id = :shipmentId")
//    //List<StopDto> findStopDataByShipmentId(@Param("shipmentId") UUID shipmentId);
//    List<StopEntity> findStopDataByShipmentId(@Param("shipmentId") UUID shipmentId);
//}

interface StopRepository {
    List<StopEntity> findStopDataByShipmentId(UUID shipmentId);
}

public class a {
}

record StopDto(Long stopId, int sequence, StopStatus status) {
}

@Service
@RequiredArgsConstructor
class ShipmentService {
    private final StopRepository stopRepo = null;

    public void handleArrival(UUID shipmentId, Long stopId) {
        List<StopEntity> stops = stopRepo.findStopDataByShipmentId(shipmentId);
        Shipment shipment = new Shipment(stops);
        shipment.arrive(stopId);
        // Aquí se podrían publicar eventos, actualizar estado, etc.
    }
}
