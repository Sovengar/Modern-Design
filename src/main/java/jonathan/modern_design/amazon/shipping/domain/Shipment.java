package jonathan.modern_design.amazon.shipping.domain;

import java.util.ArrayList;
import java.util.List;

public class Shipment {

    private final List<Stop> stops;
    private List<StopEntity> stopsEntity = new ArrayList<>();

    private Shipment(List<Stop> stops) {
        this.stops = stops;
    }

    public boolean isComplete() {
        return stops.stream().allMatch(stop -> stop.getStatus() == Stop.Status.DEPARTED);
    }

    public void arrive(Long stopId) {
        var currentStop = getCurrentStopOrElseThrow(stopId);

        boolean previousAreNotDeparted = stops.stream()
                .anyMatch(stop -> stop.getStatus() != Stop.Status.DEPARTED && stop.getSequence() < currentStop.getSequence());

        if (previousAreNotDeparted) {
            throw new IllegalStateException("Previous stops have not departed");
        }

        if (currentStop.getStatus() != Stop.Status.IN_TRANSIT) {
            throw new IllegalStateException("Current stop is not in transit");
        }

        currentStop.arrive();
    }

    public void pickup(Long stopId) {
        var currentStop = getCurrentStopOrElseThrow(stopId);

        if (!(currentStop instanceof PickupStop)) {
            throw new IllegalStateException("Stop is not a pickup.");
        }

        currentStop.depart();
    }

    public void deliver(Long stopId) {
        var currentStop = getCurrentStopOrElseThrow(stopId);

        if (!(currentStop instanceof DeliveryStop)) {
            throw new IllegalStateException("Stop is not a delivery.");
        }

        currentStop.depart();
    }

    private Stop getCurrentStopOrElseThrow(final Long stopId) {
        return stops.stream()
                .filter(stop -> stop.getStopId().equals(stopId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Stop not found"));
    }

    //private StopEntity getCurrentStopOrElseThrow(final Long stopId) {
    //    return stopsEntity.stream()
    //            .filter(stop -> stop.getId().equals(stopId))
    //            .findFirst()
    //            .orElseThrow(() -> new IllegalStateException("Stop not found"));
    //}

    //TODO ADD THIS ON ORDER AR
    //public Shipment Ship(LocalDateTime expectedPickup, LocalDateTime expectedDelivery) {
    //    var pickup = new PickupStop(expectedPickup);
    //    var delivery = new DeliveryStop(expectedDelivery);
    //    return new Shipment.Factory(pickup, delivery);
    //}
    public static class Factory {
        public static Shipment create(PickupStop pickup, DeliveryStop delivery) {
            return new Shipment(List.of(pickup, delivery));
        }

        public static Shipment create(List<Stop> stops) {
            if (stops.size() < 2) {
                throw new IllegalArgumentException("Shipment must have at least 2 stops.");
            }

            if (!(stops.getFirst() instanceof PickupStop)) {
                throw new IllegalArgumentException("First stop must be a pickup.");
            }

            if (!(stops.getLast() instanceof DeliveryStop)) {
                throw new IllegalArgumentException("Last stop must be a delivery.");
            }

            return new Shipment(stops);
        }
    }
}
