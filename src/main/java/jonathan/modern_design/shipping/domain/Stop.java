package jonathan.modern_design.shipping.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class Stop {
    protected int stopId;
    protected Status status = Status.IN_TRANSIT;
    protected Address address;
    protected LocalDateTime scheduled;
    protected LocalDateTime departed;

    public Stop(int stopId, Address address, LocalDateTime scheduled) {
        this.stopId = stopId;
        this.address = address;
        this.scheduled = scheduled;
    }

    public void arrive() {
        if (status != Status.IN_TRANSIT) {
            throw new IllegalStateException("Stop has already arrived.");
        }

        this.status = Status.ARRIVED;
    }

    public void depart() {
        if (status == Status.DEPARTED) {
            throw new IllegalStateException("Stop has already departed.");
        }

        if (status == Status.IN_TRANSIT) {
            throw new IllegalStateException("Stop hasn't arrived yet.");
        }

        this.status = Status.DEPARTED;
        this.departed = LocalDateTime.now();
    }

    public enum Status {
        IN_TRANSIT,
        DEPARTED,
        ARRIVED
    }
}

class PickupStop extends Stop {
    public PickupStop(int stopId, Address address, LocalDateTime scheduled) {
        super(stopId, address, scheduled);
    }
}

class DeliveryStop extends Stop {
    public DeliveryStop(int stopId, Address address, LocalDateTime scheduled) {
        super(stopId, address, scheduled);
    }
}

class Address {
    private String street;
    private String city;
    private String zipCode;

    // Constructores, getters y setters
}
