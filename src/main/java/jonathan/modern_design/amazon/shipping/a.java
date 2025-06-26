package jonathan.modern_design.amazon.shipping;

import jonathan.modern_design.amazon.shipping.domain.Shipment;
import jonathan.modern_design.amazon.shipping.domain.Stop;
import jonathan.modern_design.amazon.shipping.domain.StopEntity;
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

record StopDto(int stopId, int sequence, Stop.Status status) {
}

@Service
@RequiredArgsConstructor
class ShipmentService {
    private final StopRepository stopRepo = null;

    public void handleArrival(UUID shipmentId, int stopId) {
        List<StopEntity> stops = stopRepo.findStopDataByShipmentId(shipmentId);
        var shipment = Shipment.Factory.createWithEntity(stops);
        shipment.arrive(stopId);
        // Aquí se podrían publicar eventos, actualizar estado, etc.
    }
}
