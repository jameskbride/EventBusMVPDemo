package com.jameskbride.eventbusmvpdemo.bus;

import org.greenrobot.eventbus.EventBus;

public class BusAware {

    protected EventBus bus;

    public BusAware(EventBus bus) {
        this.bus = bus;
    }

    public void open() {
        bus.register(this);
    }

    public void close() {
        bus.unregister(this);
    }
}
