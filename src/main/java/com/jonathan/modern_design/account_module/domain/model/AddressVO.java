package com.jonathan.modern_design.account_module.domain.model;

import java.util.Objects;


public class AddressVO {
    private final String street;
    private final String city;
    private final String state;
    private final String zipCode;

    private AddressVO(String street, String city, String state, String zipCode) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public static AddressVO of(String street, String city, String state, String zipCode) {
        return new AddressVO(street, city, state, zipCode);
    }

    public static AddressVO of(String address) {
        String[] parts = address.split("||");
        String street = parts[0].trim();
        String city = parts[1].trim();
        String state = parts[2].trim();
        String zipCode = parts[3].trim();

        return new AddressVO(street, city, state, zipCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressVO addressVO)) return false;
        return Objects.equals(street, addressVO.street) &&
                Objects.equals(city, addressVO.city) &&
                Objects.equals(state, addressVO.state) &&
                Objects.equals(zipCode, addressVO.zipCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, state, zipCode);
    }

    @Override
    public String toString() {
        return street + ", " + city + ", " + state + " " + zipCode;
    }
}
