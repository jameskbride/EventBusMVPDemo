package com.jameskbride.eventbusmvpdemo.bus;

public class NetworkErrorEvent {
    private NetworkRequestEvent networkRequestEvent;

    public NetworkErrorEvent(NetworkRequestEvent networkRequestEvent) {
        this.networkRequestEvent = networkRequestEvent;
    }

    public NetworkRequestEvent getNetworkRequestEvent() {
        return networkRequestEvent;
    }
}
