package jonathan.modern_design.shipping.domain;

import java.util.List;
import java.util.UUID;

public class Shipment {

    private final List<StopEntity> stops;

    public Shipment(List<StopEntity> stops) {
        this.stops = stops;
    }

    public boolean isComplete() {
        return stops.stream().allMatch(stop -> stop.status() == StopStatus.DEPARTED);
    }

    public void arrive(Long stopId) {
        StopEntity currentStop = stops.stream()
                .filter(stop -> stop.id().equals(stopId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Stop not found"));

        boolean previousNotDeparted = stops.stream()
                .anyMatch(stop -> stop.sequence() < currentStop.sequence() && stop.status() != StopStatus.DEPARTED);

        if (previousNotDeparted) {
            throw new IllegalStateException("Previous stops have not departed");
        }

        if (currentStop.status() != StopStatus.IN_TRANSIT) {
            throw new IllegalStateException("Current stop is not in transit");
        }

        // dominio no modifica directamente, solo expresa reglas y podría emitir eventos
        // TODO currentStop.arrive(); ???
    }

    public void pickup(UUID stopId) {

    }

    public void deliver(UUID stopId) {

    }
}
