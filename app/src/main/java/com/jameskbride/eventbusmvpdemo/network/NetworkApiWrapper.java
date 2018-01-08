package com.jameskbride.eventbusmvpdemo.network;

import com.jameskbride.eventbusmvpdemo.bus.NetworkErrorEvent;
import com.jameskbride.eventbusmvpdemo.bus.NetworkRequestEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import io.reactivex.functions.Consumer;

public class NetworkApiWrapper<T extends Throwable> implements Consumer<T> {

    private EventBus eventBus;
    private Consumer consumer;
    private NetworkRequestEvent networkRequestEvent;

    public NetworkApiWrapper(EventBus eventBus, Consumer consumer, NetworkRequestEvent networkRequestEvent) {
        this.eventBus = eventBus;
        this.consumer = consumer;
        this.networkRequestEvent = networkRequestEvent;
    }

    @Override
    public void accept(T t) throws Exception {
        if (t instanceof IOException) {
            eventBus.post(new NetworkErrorEvent(networkRequestEvent));
        } else {
            consumer.accept(t);
        }
    }
}
