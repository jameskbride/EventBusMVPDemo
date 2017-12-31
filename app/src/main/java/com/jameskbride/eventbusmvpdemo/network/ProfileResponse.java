package com.jameskbride.eventbusmvpdemo.network;

import java.io.Serializable;
import java.util.List;

public class ProfileResponse implements Serializable {
    private final String firstName;
    private final String lastName;
    private final String addressLine1;
    private final String addressLine2;
    private final String city;
    private final String state;
    private final String zipCode;
    private final List<Order> orderHistory;

    public ProfileResponse(
            String firstName,
            String lastName,
            String addressLine1,
            String addressLine2,
            String city,
            String state,
            String zipCode,
            List<Order> orderHistory) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.orderHistory = orderHistory;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public List<Order> getOrderHistory() {
        return orderHistory;
    }
}