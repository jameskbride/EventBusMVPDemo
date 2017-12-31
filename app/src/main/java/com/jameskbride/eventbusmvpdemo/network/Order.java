package com.jameskbride.eventbusmvpdemo.network;

import java.io.Serializable;


public class Order implements Serializable {

    private final Integer id;
    private final String description;

    public Order(Integer id, String description) {

        this.id = id;
        this.description = description;
    }
}
