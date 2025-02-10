package com.jonathan.modern_design.account_module.domain.model;

import java.util.Objects;


public class AccountAddressVO {
    private final String street;
    private final String city;
    private final String state;
    private final String zipCode;

    private AccountAddressVO(String street, String city, String state, String zipCode) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public static AccountAddressVO of(String street, String city, String state, String zipCode) {
        return new AccountAddressVO(street, city, state, zipCode);
    }

    public static AccountAddressVO of(String address) {
        String[] parts = address.split("||");
        String street = parts[0].trim();
        String city = parts[1].trim();
        String state = parts[2].trim();
        String zipCode = parts[3].trim();
        //TODO FIX
        return new AccountAddressVO(street, city, state, zipCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountAddressVO accountAddressVO)) return false;
        return Objects.equals(street, accountAddressVO.street) &&
                Objects.equals(city, accountAddressVO.city) &&
                Objects.equals(state, accountAddressVO.state) &&
                Objects.equals(zipCode, accountAddressVO.zipCode);
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
