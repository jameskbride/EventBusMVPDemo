package com.jameskbride.eventbusmvpdemo.bus;

public class GetProfileEvent extends NetworkRequestEvent {

    private String id;

    public GetProfileEvent(String id) {

        this.id = id;
    }

    public String getId() {
        return id;
    }
}
